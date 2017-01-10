package com.fanzhuan.appserver.service.order;

import com.fanzhuan.appserver.result.BaseResult;
import com.fanzhuan.services.base.api.model.orders.Order;
import com.fanzhuan.services.base.api.model.orders.OrderPayInfo;

public interface WxpayService {

	public BaseResult unifiedorder(Order order, OrderPayInfo payInfo);

	/**
	 * 微信支付异步回调
	 *
	 * @param response
	 */
	public BaseResult wxpayAsyncNotify(String xml);

	/**
	 * 检查订单状态
	 *
	 * @param tradeNo
	 * @return
	 */
	public BaseResult checkOrder(String tradeNo);

}
