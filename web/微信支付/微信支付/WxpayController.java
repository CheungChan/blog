package com.fanzhuan.appserver.controller.order;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fanzhuan.appserver.controller.BaseController;
import com.fanzhuan.appserver.interceptor.NotValidateLogin;
import com.fanzhuan.appserver.result.BaseResult;
import com.fanzhuan.appserver.service.order.WxpayService;
import com.fanzhuan.framework.commons.utils.XmlUtil;
import com.fanzhuan.services.base.api.model.orders.OrderPayInfo;

@Controller
@RequestMapping("wxpay")
public class WxpayController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private WxpayService wxpayService;

	@RequestMapping("/notify")
	@NotValidateLogin
	public void notify(HttpServletRequest request, HttpServletResponse response) {

		String xml = null;
		try {
			xml = getRequestBodyByReader(request);
		} catch (IOException e) {
			logger.error("【微信支付】异步回调，获取Xml请求参数异常：", e);
			return;
		}
		logger.info("接收到【微信支付】异步回调：{}", xml);

		BaseResult result = wxpayService.wxpayAsyncNotify(xml);

		Map<String, Object> resultMap = new HashMap<>();

		if (result.getStatus() == OrderPayInfo.RESULT_OK || result.getStatus() == OrderPayInfo.RESULT_RECHARGE_SUCCESS
				|| result.getStatus() == OrderPayInfo.RESULT_OK_REPEAT) {
			resultMap.put("return_code", "SUCCESS");
			resultMap.put("return_msg", "OK");
		} else {
			resultMap.put("return_code", "FAIL");
			resultMap.put("return_msg", result.getMessage());
		}
		PrintWriter writer = null;
		try {
			String resultXml = XmlUtil.map2Xml(resultMap);

			writer = response.getWriter();
			writer.print(resultXml);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				writer.close();
			}
		}

	}

}
