package com.fanzhuan.appserver.pay.wxpay;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.fanzhuan.framework.commons.utils.XmlUtil;

/**
 * 功能：支付宝MD5签名处理核心文件，不需要修改 版本：3.3 修改日期：2012-08-17 说明：
 * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 * 该代码仅供学习和研究支付宝接口使用，只是提供一个
 */

public class WxpayMD5 {

	/**
	 * 签名字符串
	 *
	 * @param text
	 *            需要签名的字符串
	 * @param key
	 *            密钥
	 * @param input_charset
	 *            编码格式
	 * @return 签名结果
	 */
	public static String sign(String text, String key, String input_charset) {
		text = text + "&key=" + key;
		return DigestUtils.md5Hex(getContentBytes(text, input_charset)).toUpperCase();
	}

	/**
	 * 签名字符串
	 *
	 * @param text
	 *            需要签名的字符串
	 * @param sign
	 *            签名结果
	 * @param key
	 *            密钥
	 * @param input_charset
	 *            编码格式
	 * @return 签名结果
	 */
	public static boolean verify(String text, String sign, String key, String input_charset) {
		String mysign = sign(text, key, input_charset);
		System.out.println(mysign);
		if (mysign.equals(sign)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param content
	 * @param charset
	 * @return
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 */
	private static byte[] getContentBytes(String content, String charset) {
		if (charset == null || "".equals(charset)) {
			return content.getBytes();
		}
		try {
			return content.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
		}
	}

	public static void main(String[] args) {
		String xml = "<xml><appid><![CDATA[wx1ad48619e1ebb368]]></appid><bank_type><![CDATA[CFT]]></bank_type><fee_type><![CDATA[CNY]]></fee_type><mch_id><![CDATA[1320517801]]></mch_id><nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str><openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid><out_trade_no><![CDATA[99d5c3ced4794c19a388f4c808b61b7c]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[64B651EF7477DF9800E8169799BB71ED]]></sign><time_end><![CDATA[20160407151540]]></time_end><total_fee>12312</total_fee><trade_type><![CDATA[NATIVE]]></trade_type><transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id></xml>";
		Map<String, Object> map = XmlUtil.xml2Map(xml);
		System.out.println(map);
		String oSign = String.valueOf(map.get("sign"));

		map = WxpayCore.paraFilter(map);
		String str = WxpayCore.createLinkString(map);
		System.out.println(str);
		String sign = sign(str, WxpayConfig.key, WxpayConfig.input_charset);
		System.out.println(sign);
		System.out.println(verify(str, oSign, WxpayConfig.key, WxpayConfig.input_charset));

	}
}