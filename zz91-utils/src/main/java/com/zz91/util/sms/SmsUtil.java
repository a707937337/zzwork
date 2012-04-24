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


public class SmsUtil {
	private static String SMS_CONFIG = "mailclient.properties";
	private static String API_HOST="http://apps.zz91.com/zz91-sms/";
	private static SmsUtil _instance;	
	private static Logger LOG = Logger.getLogger(SmsUtil.class);
	public final static int PRIORITY_HEIGHT = 0;
    public final static int PRIORITY_DEFAULT = 1;
    public final static int PRIORITY_TASK = 10;
	
	public synchronized static SmsUtil getInstance() {
		if (_instance == null) {
			_instance = new SmsUtil();
		}
			return _instance;
	}
	
	public void init(){
		init(SMS_CONFIG);
	}
	private void init(String properties) {
		if (StringUtils.isEmpty(properties)) {
			properties = SMS_CONFIG;
		}
		try{
		@SuppressWarnings("unchecked")
		Map<String, Object> map = FileUtils.readPropertyFile(properties, HttpUtils.CHARSET_UTF8);
			API_HOST = (String) map.get("sms.host");
			} catch (IOException e) {
				LOG.error("An error occurred when load sms properties:" + properties, e);
		}
	}

	public void sendSms(String templateCode,String receiver,
			Date gmtSend,String gatewayCode,Integer priority,String content,Map<String, Object>smsParameter){
		if(priority==null){
			priority=PRIORITY_DEFAULT;
		}
		try {
		JSONObject js=JSONObject.fromObject(smsParameter);
		NameValuePair[]data ={
				new NameValuePair("templateCode",templateCode),
				new NameValuePair("receiver",receiver),
				new NameValuePair("gmtSendStr",DateUtil.toString(gmtSend, "yyyy-MM-dd HH:mm:ss")),
				new NameValuePair("gatewayCode",gatewayCode),
				new NameValuePair("priority", priority.toString()),
				new NameValuePair("content",content),
				new NameValuePair("dataMap",js.toString())
				};
			HttpUtils.getInstance().httpPost(API_HOST + "sms/send.htm", data, HttpUtils.CHARSET_UTF8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void sendSms(String templateCode,String receiver,String content,String gatewayCode,Date gmtSend){
		
		sendSms(templateCode,receiver, gmtSend, gatewayCode, null, content, null);
	}
	
	public static void main(String[] args) throws ParseException{
		API_HOST = "http://test.zz91.com:8080/sms/";
		SmsUtil.getInstance().sendSms("123","13486386720", "ni hao ", "123",DateUtil.getDate("2012-04-01", "yyyy-MM-dd"));
	}
}
