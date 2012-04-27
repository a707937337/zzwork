package com.zz91.util.sms;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import net.sf.json.JSONObject;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.Logger;

import com.zz91.util.datetime.DateUtil;
import com.zz91.util.file.FileUtils;
import com.zz91.util.http.HttpUtils;
import com.zz91.util.lang.StringUtils;

/**
 * <br />
 * SmsUtil 短信发送工具包 使用方法: <br />
 * 
 * 第一步：在配置文件中配置邮件api host，默认：smsclient.properties <br />
 * 例：sms.host = http://apps.zz91.com/zz91-sms/ <br />
 * 
 * 第二步：初始化工具 <br />
 * SmsUtil.getInstance().init("web.properties"); <br />
 * 或 SmsUtil.getInstance().init(); 表示初始化smsclient.properties <br />
 * 
 * 第三步: 准备数据
 * templateCode: 模板code, 如果不填，则不使用模板 <br />
 * receiver: 接收电话, 必填 <br />
 * gmtSend: 定时发送短信, 不填则放入发送队列后发送短信 <br />
 * gatewayCode:	网关code <br />
 * priority: 优先级 <br />
 * content: 短信内容 <br />
 * 
 * 第四步: 选择短信调用方法, 发送短信 SmsUtil.getInstance().sendSms(xxx,xxx,xxx,xxx,xxx);
 * 
 * @author root
 *
 */
public class SmsUtil {
	private static String SMS_CONFIG = "smsclient.properties";
	private static String API_HOST = "http://apps.zz91.com/zz91-sms/";
	private static SmsUtil _instance;
	private static Logger LOG = Logger.getLogger(SmsUtil.class);
	public final static int PRIORITY_HEIGHT = 0;
	public final static int PRIORITY_DEFAULT = 1;
	public final static int PRIORITY_TASK = 10;
	private static String DEFAULT_GATEWAY = "emay_jar";

	public synchronized static SmsUtil getInstance() {
		if (_instance == null) {
			_instance = new SmsUtil();
		}
		return _instance;
	}

	public void init() {
		init(SMS_CONFIG);
	}

	private void init(String properties) {
		if (StringUtils.isEmpty(properties)) {
			properties = SMS_CONFIG;
		}
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = FileUtils.readPropertyFile(properties,
					HttpUtils.CHARSET_UTF8);
			API_HOST = (String) map.get("sms.host");
		} catch (IOException e) {
			LOG.error("An error occurred when load sms properties:"
					+ properties, e);
		}
	}

	/**
	 * 短信发送 参数
	 * 
	 * @param templateCode
	 *            模板code
	 * @param receiver
	 *            接收电话
	 * @param gmtSend
	 *            发送时间
	 * @param gatewayCode
	 *            网关code
	 * @param priority
	 *            优先级
	 * @param content
	 *            内容
	 * @param smsParameter
	 *            转换参数
	 */
	public void sendSms(String templateCode, String receiver, Date gmtSend,
			String gatewayCode, Integer priority, String content,
			Map<String, Object> smsParameter) {
		if (priority == null) {
			priority = PRIORITY_DEFAULT;
		}
		if (gmtSend == null) {
			gmtSend = new Date();
		}
		if (StringUtils.isEmpty(gatewayCode)) {
			gatewayCode = DEFAULT_GATEWAY;
		}
		try {
			JSONObject js = JSONObject.fromObject(smsParameter);
			NameValuePair[] data = {
					new NameValuePair("templateCode", templateCode),
					new NameValuePair("receiver", receiver),
					new NameValuePair("gmtSendStr", DateUtil.toString(gmtSend,
							"yyyy-MM-dd HH:mm:ss")),
					new NameValuePair("gatewayCode", gatewayCode),
					new NameValuePair("priority", priority.toString()),
					new NameValuePair("content", content),
					new NameValuePair("dataMap", js.toString()) };
			HttpUtils.getInstance().httpPost(API_HOST + "sms/send.htm", data,
					HttpUtils.CHARSET_UTF8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 直接简单发送(加发送时间)
	 * 
	 * @param receiver
	 *            接收电话
	 * @param content
	 *            内容
	 * @param gmtSend
	 *            发送时间
	 */
	public void sendSms(String receiver, String content, Date gmtSend) {
		sendSms(null, receiver, gmtSend, null, null, content, null);
	}

	/**
	 * 简单发送
	 * 
	 * @param receiver
	 *            接收电话
	 * @param content
	 *            内容
	 * @param gmtSend
	 *            发送时间
	 */
	public void sendSms(String receiver, String content) {
		sendSms(null, receiver, null, null, null, content, null);
	}

	/**
	 * 根据模板编号发送
	 * 
	 * @param templateCode
	 *            模板编号
	 * @param receiver
	 *            接收电话
	 * @param content
	 *            内容
	 * @param gmtSend
	 *            发送时间
	 */

	public void sendSms(String templateCode, String receiver, String content,
			Date gmtSend) {

		sendSms(templateCode, receiver, gmtSend, null, null, content, null);
	}

	public static void main(String[] args) throws ParseException {
		API_HOST = "http://web.zz91.com:8080/sms/";
		SmsUtil.getInstance().sendSms("13738194812", "我是一个并【zz91】");
	}
}
