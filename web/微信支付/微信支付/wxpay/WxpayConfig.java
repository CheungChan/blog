package com.fanzhuan.appserver.pay.wxpay;

/**
 * 微信支付基础信息配置
 *
 * @author Lee
 *
 */
public class WxpayConfig {

	// 统一下单地址
	public static final String unifiedorder = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	// 查询订单
	public static final String orderquery = "https://api.mch.weixin.qq.com/pay/orderquery";

	/* ============= 基本参数配置 ============ */
	// 公众账号ID
	public static final String appid = "wx1ad48619e1ebb368";
	public static final String appid_app = "wx6258b57fc65f068b";
	// 商户号
	public static final String mch_id = "1320517801";
	public static final String mch_id_app = "1373248202";
	// 设备号
	public static final String device_info = "WEB";
	// 交易类型
	public static final String trade_type_native = "NATIVE";// 原生扫码
	public static final String trade_type_app = "APP";// app端
	public static final String trade_type_wap = "WAP";// wap端
	// 字符编码格式 目前支持 gbk 或 utf-8
	public static final String input_charset = "utf-8";
	// 商户的私钥
	public static final String key = "003b2ad24e5b452b8238fc8f65643258";
	public static final String key_app = "827c053583ecf7da8fb33f1255d9a1bc";
	// 异步回调地址
	public static final String notify_url = "/wxpay/notify";

	public static final String ip = "61.160.234.51";

	/* ============= 业务参数配置 ============ */
	public static final String RETURN_SUCCESS = "SUCCESS";

	public static final String RESULT_SUCCESS = "SUCCESS";

}
