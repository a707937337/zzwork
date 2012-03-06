package com.zz91.util.log;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;

import com.zz91.util.file.FileUtils;
import com.zz91.util.http.HttpUtils;


/**LogUtil是一个记录应用系统操作行为的一个系统
 * 通过调用客户端log方法你可以传入自定义的参数来完成你的日志保存行为
 * 使用方法：log.client.host和log.appcode两个参数
 * 第一步：在web.properties文件中配置
 * 第二步：通过LogUtil.getInstance()得到LogUtil实例
 * 第二步：通过调用LogUtil实例的init()初始化host以及appcode信息
 * 第三步：通过调用LogUtil实例的log方法来记录日志信息
 * @author Leon
 *
 */
public class LogUtil {
	
	private static LogUtil _instances = null;
	private static String DEFAULT_PROP="logclient.properties";
	private static String APP_CODE="log";
	private static String HOST="http://192.168.110.119:8201/zz91-log";
	
	private LogUtil() {}
	/**
	 * 获取LogUtil实例
	 * @return
	 */
	public static synchronized LogUtil getInstance() {
		if (_instances == null) {
			_instances = new LogUtil();
		}
		return _instances;
	}
	

	/**
	 * 初始化方法
	 * @param properties
	 */
	public void init(){
		init(DEFAULT_PROP);
	}
	
	@SuppressWarnings("unchecked")
	public void init(String properties){
		Map<String, String> map=null;
		try {
			map = FileUtils.readPropertyFile(properties, "utf-8");
		} catch (IOException e) {
		}
		if(map!=null){
			HOST = map.get("log.host");
			APP_CODE=map.get("log.appcode");
		}
	}
	
	public void log(String operator, String operation, String ip, String data){
		NameValuePair[] param=new NameValuePair[]{
				new NameValuePair("appCode", APP_CODE),
				new NameValuePair("operator", operator),
				new NameValuePair("operation", operation),
				new NameValuePair("ip", ip),
				new NameValuePair("time", String.valueOf(System.currentTimeMillis())),
				new NameValuePair("data", data)
				};
		
		try {
			HttpUtils.getInstance().httpPost(HOST+"/log", param, HttpUtils.CHARSET_UTF8);
		} catch (HttpException e) {
		} catch (IOException e) {
		}
		
	}
	
	public void log(String operator, String operation, String ip){
		log(operator, operation, ip, null);
	}
	
	public void log(String operator, String operation){
		log(operator, operation, null, null);
	}
	
	public static void main(String[] args) {
		LogUtil logutil=new LogUtil();
		LogUtil.HOST="http://127.0.0.1:580/zz91-log";
		logutil.log("mays", "testzhcn", "127.0.0.1", "中文的看下可不可以啊");
		logutil.log("mays", "testzhcn", "127.0.0.1", "中文的看下可不可以啊2");
		logutil.log("mays", "testzhcn", "127.0.0.1", "中文的看下可不可以啊3");
		logutil.log("mays", "testzhcn", "127.0.0.1", "中文的看下可不可以啊4");
		logutil.log("mays", "testzhcn", "127.0.0.1", "中文的看下可不可以啊5");
		logutil.log("mays", "testzhcn", "127.0.0.1", "中文的看下可不可以啊6");
		logutil.log("mays", "testzhcn", "127.0.0.1", "中文的看下可不可以啊7");
		logutil.log("mays", "testzhcn", "127.0.0.1", "中文的看下可不可以啊8");
	}
}
