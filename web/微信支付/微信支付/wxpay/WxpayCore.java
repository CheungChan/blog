package com.fanzhuan.appserver.pay.wxpay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fanzhuan.framework.commons.utils.BCryptUtil;
import com.fanzhuan.framework.commons.utils.CollectionUtils;
import com.fanzhuan.framework.commons.utils.HttpUtil;
import com.fanzhuan.framework.commons.utils.XmlUtil;

public class WxpayCore {

	public static String buildUnifiedorderXml(String host, String productName, String tradeNo, long totalFee,
			String userIP, String tradeType) {
		Map<String, Object> paramMap = new HashMap<>();
		// 基础参数
		if (WxpayConfig.trade_type_app.equals(tradeType)) {
			paramMap.put("appid", WxpayConfig.appid_app);
			paramMap.put("mch_id", WxpayConfig.mch_id_app);
		} else {
			paramMap.put("appid", WxpayConfig.appid);
			paramMap.put("mch_id", WxpayConfig.mch_id);
		}
		// paramMap.put("device_info", WxpayConfig.device_info);
		paramMap.put("nonce_str", BCryptUtil.randomUUID());
		paramMap.put("notify_url", host + WxpayConfig.notify_url);

		// 业务参数
		paramMap.put("out_trade_no", tradeNo);

		paramMap.put("body", productName);
		paramMap.put("total_fee", totalFee);
		paramMap.put("spbill_create_ip", userIP);
		paramMap.put("trade_type", tradeType);
		paramMap.put("attach", tradeNo);

		String xml = transToXml(paramMap, tradeType);

		return xml;
	}

	public static String buildCheckOrderXml(String tradeNo, String tradeType) {
		Map<String, Object> paramMap = new HashMap<>();
		// 基础参数
		paramMap.put("appid", WxpayConfig.appid);
		paramMap.put("mch_id", WxpayConfig.mch_id);
		paramMap.put("device_info", WxpayConfig.device_info);
		paramMap.put("nonce_str", BCryptUtil.randomUUID());

		// 业务参数
		paramMap.put("out_trade_no", tradeNo);

		String xml = transToXml(paramMap, tradeType);

		return xml;
	}

	/**
	 * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
	 *
	 * @param params
	 *            需要排序并参与字符拼接的参数组
	 * @return 拼接后字符串
	 */
	public static String createLinkString(Map<String, Object> params) {
		// 排序
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);

		// System.out.println("排序：" + keys);

		// 拼接
		StringBuffer buffer = new StringBuffer();
		if (!CollectionUtils.isEmpty(params)) {
			int offset = params.size() - 1;
			for (int i = 0; i < offset; i++) {
				String key = keys.get(i);
				Object value = params.get(key);
				buffer.append(key).append("=").append(value).append("&");
			}
			buffer.append(keys.get(offset)).append("=").append(params.get(keys.get(offset)));
		}

		return buffer.toString();
	}

	/**
	 * 除去数组中的空值和签名参数
	 *
	 * @param sArray
	 *            签名参数组
	 * @return 去掉空值与签名参数后的新签名参数组
	 */
	public static Map<String, Object> paraFilter(Map<String, Object> sArray) {
		Map<String, Object> result = new HashMap<>();
		if (sArray == null || sArray.size() <= 0) {
			return result;
		}
		for (String key : sArray.keySet()) {
			Object value = sArray.get(key);
			if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
				continue;
			}
			result.put(key, value);
		}
		return result;
	}

	/**
	 * 附加签名，转换成XML
	 * 
	 * @param xmlMap
	 * @return
	 */
	private static String transToXml(Map<String, Object> xmlMap, String tradeType) {
		// 过滤
		xmlMap = paraFilter(xmlMap);

		// 参数拼接（不含签名）
		String paramStr = createLinkString(xmlMap);
		// System.out.println("参数拼接（不含签名）:" + paramStr);

		// 生成签名
		String key = WxpayConfig.key_app;
		if (!WxpayConfig.trade_type_app.equals(tradeType)) {
			key = WxpayConfig.key;
		}
		String sign = WxpayMD5.sign(paramStr, key, WxpayConfig.input_charset);
		// System.out.println(sign);

		// 加入签名参数
		xmlMap.put("sign", sign);

		// 生成参数拼接（含签名）
		String xml = XmlUtil.map2Xml(xmlMap);
		// System.out.println("参数拼接（含签名）" + xml);
		return xml;
	}

	public static void main(String[] args) {
		String xml = buildUnifiedorderXml("http://www.dianfuketang.com", "2016-2017语文 八年级(下) 同步课程 (人教版)",
				"003b2ad24e5b452b8238fc8f65642ef6", 3000l, "125.71.79.239", WxpayConfig.trade_type_app);
		System.out.println(xml);
		String resultXml = HttpUtil.postUseXml(WxpayConfig.unifiedorder, xml);
		System.out.println(resultXml);
	}

}
