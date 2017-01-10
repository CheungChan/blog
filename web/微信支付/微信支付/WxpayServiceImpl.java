package com.fanzhuan.appserver.service.order.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fanzhuan.appserver.pay.wxpay.WxpayConfig;
import com.fanzhuan.appserver.pay.wxpay.WxpayCore;
import com.fanzhuan.appserver.pay.wxpay.WxpayMD5;
import com.fanzhuan.appserver.result.BaseResult;
import com.fanzhuan.appserver.service.order.WxpayService;
import com.fanzhuan.appserver.utils.AppConfig;
import com.fanzhuan.framework.commons.utils.CollectionUtils;
import com.fanzhuan.framework.commons.utils.DateUtil;
import com.fanzhuan.framework.commons.utils.HttpUtil;
import com.fanzhuan.framework.commons.utils.XmlUtil;
import com.fanzhuan.services.base.api.enums.OrderPayType;
import com.fanzhuan.services.base.api.error.ErrorCode;
import com.fanzhuan.services.base.api.error.FZApiException;
import com.fanzhuan.services.base.api.model.orders.Order;
import com.fanzhuan.services.base.api.model.orders.OrderPayInfo;
import com.fanzhuan.services.base.api.model.orders.SubOrder;

@Service
public class WxpayServiceImpl extends ThirdpayBaseServiceImpl implements WxpayService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private AppConfig config;

	@Override
	public BaseResult unifiedorder(Order order, OrderPayInfo payInfo) {
		BaseResult result = new BaseResult();
		List<SubOrder> subOrders = order.getSubOrders();
		if (CollectionUtils.isEmpty(subOrders)) {
			throw new FZApiException(ErrorCode.ORDER_SUB_NOTEXIST_ERROR);
		}

		Long orderId = order.getId();
		SubOrder subOrder = subOrders.get(0);
		String xml = WxpayCore.buildUnifiedorderXml(config.getHost(), subOrder.getGoodName(), payInfo.getTradeNo(),
				payInfo.getPayMoney(), order.getIp(), WxpayConfig.trade_type_app);

		logger.info("发送微信支付参数：{}", xml);

		String responseBody = HttpUtil.postUseXml(WxpayConfig.unifiedorder, xml);

		Map<String, Object> resultMap = XmlUtil.xml2Map(responseBody);

		if (WxpayConfig.RETURN_SUCCESS.equals(resultMap.get("return_code"))) {
			if (WxpayConfig.RESULT_SUCCESS.equals(resultMap.get("result_code"))) {

				String prepayId = String.valueOf(resultMap.get("prepay_id"));
				if (StringUtils.isEmpty(prepayId)) {
					logger.error("微信支付，订单[{}]，获取预支付交易会话标识为空", orderId);
					result.setStatus(1);
					result.setMessage("预支付交易会话标识");
				} else {
					result.setStatus(0);
					result.setData(prepayId);
				}
			} else {
				logger.error("微信支付，下单失败，订单[{}]，result_code:{}，err:{}->{}", orderId, resultMap.get("result_code"),
						resultMap.get("err_code"), resultMap.get("err_code_des"));
				if ("ORDERPAID".equals(String.valueOf(resultMap.get("err_code")))) {
					result.setStatus(100);
					result.setMessage("该订单已支付");
				} else {
					result.setStatus(1);
					result.setMessage(resultMap.get("err_code") + ":" + resultMap.get("err_code_des"));
				}
			}
		} else {
			logger.error("微信支付，下单失败，订单[{}]，return_code:{}，return_msg:{}", orderId, resultMap.get("return_code"),
					resultMap.get("return_msg"));
			result.setStatus(1);
			result.setMessage(resultMap.get("return_code") + ":" + resultMap.get("return_msg"));
		}

		return result;
	}

	@Override
	public BaseResult checkOrder(String tradeNo) {
		String checkXml = doCheckOrder(tradeNo);

		Map<String, Object> resultMap = XmlUtil.xml2Map(checkXml);

		if (!WxpayConfig.RETURN_SUCCESS.equals(resultMap.get("return_code"))) {
			logger.error("微信查询订单，返回状态码：{}，返回信息：{}", resultMap.get("return_code"), resultMap.get("return_msg"));
			return new BaseResult(-2, "返回失败");
		}

		String result_code = String.valueOf(resultMap.get("result_code"));
		if (!WxpayConfig.RESULT_SUCCESS.equals(result_code)) {
			logger.error("微信查询订单，业务结果，result_code：{}，错误信息：{}->{}", result_code, resultMap.get("err_code"),
					resultMap.get("err_code_des"));
			return new BaseResult(-1, "交易失败");
		}

		String trade_state = String.valueOf(resultMap.get("trade_state"));
		if (!WxpayConfig.RESULT_SUCCESS.equals(trade_state)) {
			logger.error("微信查询订单，交易状态，trade_state：{}，状态描述：{}", trade_state, resultMap.get("trade_state_desc"));
			return new BaseResult(-4, "交易失败");
		}

		boolean verify = verifySign(resultMap, String.valueOf(resultMap.get("sign")));
		if (!verify) {
			logger.error("微信查询订单，签名错误：{}", resultMap);
			return new BaseResult(-3, "签名错误");
		}

		OrderPayInfo payInfo = orderService.getPayInfoByTradeNo(tradeNo.toString());
		if (payInfo == null) {
			logger.error("微信查询订单，订单支付信息不存在，tradeNo：{}", tradeNo);
			return new BaseResult(-8, "订单支付信息异常");
		}

		Order order = orderService.getOrderDetail(payInfo.getOrderId());
		if (order == null) {
			logger.error("微信查询订单，订单不存在，orderId：{}", payInfo.getOrderId());
			return new BaseResult(-6, "订单信息异常");
		}

		long realPayMoney = NumberUtils.toLong(String.valueOf(resultMap.get("total_fee")));

		// 微信交易号
		String wxTradeNo = String.valueOf(resultMap.get("transaction_id"));

		Date payTime = DateUtil.parse(String.valueOf(resultMap.get("time_end")), DateUtil.yyyyMMddHHMMSS);

		if (payInfo.getPayMoney() == null || payInfo.getPayMoney().longValue() != realPayMoney) {
			logger.error("微信支付，异步回调，支付金额不符，orderId：{},payMoney:{},realPayMoney", order.getId(), payInfo.getPayMoney(),
					realPayMoney);
			return rechange(order, payInfo, OrderPayType.WEIXINPAY, wxTradeNo, payTime, realPayMoney, -7,
					"支付金额异常，且充值失败");
		}

		BaseResult result = doPay(OrderPayType.WEIXINPAY, "微信查询订单", order, payInfo, wxTradeNo, payTime, realPayMoney);

		return result;
	}

	@Override
	public BaseResult wxpayAsyncNotify(String xml) {
		if (StringUtils.isEmpty(xml)) {
			logger.error("微信异步回调，参数异常：{}", xml);
			return new BaseResult(1, "参数异常");
		}

		Map<String, Object> requestMap = XmlUtil.xml2Map(xml);

		if (requestMap == null || requestMap.isEmpty()) {
			logger.error("微信异步回调，参数解析异常：{}", requestMap);
			return new BaseResult(1, "参数解析异常");
		}

		if (!WxpayConfig.RETURN_SUCCESS.equals(requestMap.get("return_code"))) {
			logger.error("微信异步回调，返回状态码：{}，返回信息：{}", requestMap.get("return_code"), requestMap.get("return_msg"));
			return new BaseResult(2, "返回失败");
		}

		String result_code = String.valueOf(requestMap.get("result_code"));
		if (!WxpayConfig.RESULT_SUCCESS.equals(result_code)) {
			logger.error("微信异步回调，业务结果，result_code：{}，错误信息：{}->{}", result_code, requestMap.get("err_code"),
					requestMap.get("err_code_des"));
			return new BaseResult(4, "交易失败");
		}

		boolean verify = verifySign(requestMap, String.valueOf(requestMap.get("sign")));
		if (!verify) {
			logger.error("微信异步回调，签名错误：{}", requestMap);
			return new BaseResult(3, "签名错误");
		}

		String tradeNo = requestMap.get("out_trade_no").toString();

		OrderPayInfo payInfo = orderService.getPayInfoByTradeNo(tradeNo);
		if (payInfo == null) {
			logger.error("微信支付，异步响应，订单支付信息不存在，tradeNo：{}", tradeNo);
			return new BaseResult(8, "订单支付信息异常");
		}

		Order order = orderService.getOrderDetail(payInfo.getOrderId());
		if (order == null) {
			logger.error("微信支付，异步响应，订单不存在，orderId：{}", payInfo.getOrderId());
			return new BaseResult(6, "订单信息异常");
		}

		long realPayMoney = NumberUtils.toLong(String.valueOf(requestMap.get("total_fee")));

		// 微信交易号
		String wxTradeNo = String.valueOf(requestMap.get("transaction_id"));

		Date payTime = DateUtil.parse(String.valueOf(requestMap.get("time_end")), DateUtil.yyyyMMddHHMMSS);

		if (payInfo.getPayMoney() == null || payInfo.getPayMoney().longValue() != realPayMoney) {
			logger.error("微信异步回调，支付金额不符，orderId[{}],payType[{}],payMoney[{}],realPayMoney:{}", order.getId(),
					payInfo.getPayType(), payInfo.getPayMoney(), realPayMoney);
			return rechange(order, payInfo, OrderPayType.WEIXINPAY, wxTradeNo, payTime, realPayMoney, -7,
					"支付金额异常，且充值失败");
		}

		BaseResult result = doPay(OrderPayType.WEIXINPAY, "微信异步回调", order, payInfo, wxTradeNo, payTime, realPayMoney);

		return result;
	}

	private boolean verifySign(Map<String, Object> map, String originalSign) {
		map = WxpayCore.paraFilter(map);
		String text = WxpayCore.createLinkString(map);
		boolean verify = WxpayMD5.verify(text, originalSign, WxpayConfig.key_app, WxpayConfig.input_charset);
		return verify;
	}

	private String doCheckOrder(String tradeNo) {
		String xml = WxpayCore.buildCheckOrderXml(tradeNo, WxpayConfig.trade_type_app);

		String responseBody = HttpUtil.postUseXml(WxpayConfig.orderquery, xml);

		logger.info("微信支付，检查微信订单[{}]状态:{}", xml, responseBody);
		return responseBody;
	}

	public static void main(String[] args) {
		String xml = "<xml><appid><![CDATA[wx6258b57fc65f068b]]></appid><bank_type><![CDATA[CFT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id><![CDATA[1373248202]]></mch_id><nonce_str><![CDATA[70117ee3c0b15a2950f1e82a215e812b]]></nonce_str><openid><![CDATA[o2ieWw726x6I0EsStxfs6kauHYDM]]></openid><out_trade_no><![CDATA[7146fe3e822842649487d2f43d96a9f5]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[6EC8E7CFE9B4AB33DEB9699AD7129B61]]></sign><time_end><![CDATA[20160824141509]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[APP]]></trade_type><transaction_id><![CDATA[4005822001201608242117443138]]></transaction_id></xml>";
		// String responseBody =
		// HttpUtil.postUseXml("http://192.168.2.78:6060/dianfu-web/wxpay/notify",
		// xml);
		// System.out.println(responseBody);
		Map<String, Object> map = XmlUtil.xml2Map(xml);
		String originalSign = map.get("sign").toString();
		map = WxpayCore.paraFilter(map);
		String text = WxpayCore.createLinkString(map);
		boolean verify = WxpayMD5.verify(text, originalSign, WxpayConfig.key_app, WxpayConfig.input_charset);
		System.out.println(verify);

	}
}
