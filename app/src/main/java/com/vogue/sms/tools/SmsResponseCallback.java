package com.vogue.sms.tools;

/**
 * 短信接收器回调接口
 * 
 * @author  江钰锋 0152
 * @version  [版本号, 2015年9月17日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface SmsResponseCallback {
	
	/**
	 * 返回短信内容
	 * 
	 * @param address
	 * @see [类、类#方法、类#成员]
	 */
	void onCallbackSmsContent(String address,String time,String body);
}
