package com.zz91.util.log;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

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
	private static Map<String,String> compares=new HashMap<String, String>();	
	
	static {
		//compares
		compares =new HashMap<String, String>();
		compares.put("<", "$lt" );
		compares.put("<=", "$lte");
		compares.put(">", "$gt");
		compares.put(">=", "$gte");
		compares.put("!=", "$ne");
	}
	
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
	
	/**
	 * 模糊查询
	 * @param value
	 * @return	{"$regex":"value"}
	 */
	public String mgLike(String value){
		JSONObject data=new JSONObject();
		data.put("$regex", value);
		return data.toString();
	}
	/**
	 * 逻辑运算符查询
	 * @param type	<,<=,>,>=,!=
	 * @param value
	 * @return {"$gte":value}
	 */
	public String mgCompare(String type,Object value){
		JSONObject data=new JSONObject();
		data.put(compares.get(type).toString(), value);
		return data.toString();
	}

	/**
	 * 要查询的列
	 * @param cl
	 * @return	{"column":1,"column":1}
	 * column后面可跟参数1,0.若1是指查询该列,若0是指排除该列
	 */
	public String mgColumns(String... column){
		JSONObject columns=new JSONObject();
		for(String cl:column){
			columns.put(cl, 1);
		}
		return columns.toString();
	}
	
	/**
	 * 将字符串组装成JSON格式数组的字符串
	 * @param value
	 * @return  [value,value,value,....]
	 */
	public String mgArray(String... value){
		StringBuffer strB=new StringBuffer();
		strB.append("[");
		for(int i=0;i<value.length;i++){
			strB.append(value[i]);
			if(i<value.length-1){
				strB.append(",");
			}
		}
		strB.append("]");
		return strB.toString();
	}
	/**
	 * 将key-value转换为JSON模式字符串
	 * @param key
	 * @param value
	 * @return {"key":value}
	 * 
	 */
	public String mgkv(String key,Object value){
		JSONObject jb=new JSONObject();
		jb.put(key, value);
		return jb.toString();
	}
	/**
	 * in条件查询
	 * @param obj
	 * @return {"$in":[obj,obj,obj,obj]}
	 */
	public String mgIn(Object... obj){
		JSONObject jb=new JSONObject();
		jb.put("$in", obj);
		return jb.toString();
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
			HttpUtils.getInstance().httpPost(HOST, param, HttpUtils.CHARSET_UTF8);
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
		long startTime = System.currentTimeMillis();
		LogUtil logutil=new LogUtil();
		
		//select test
		LogUtil.HOST="http://127.0.0.1:580/zz91-log/logRead";
		//param
		JSONObject param=new JSONObject();
		param.put("operator", logutil.mgLike("caozuorenyuan"));	//模糊查询
		param.put("appCode", "log");
		param.put("time",logutil.mgCompare(">=", "1345789546867"));	//逻辑运算符查询
		param.put("columns", logutil.mgColumns("appCode","time"));		//要查询的列,可不指定
		//param.put("sort", "time");							//排序字段
		//param.put("dir", "desc");							//排序 asc,desc
		param.put("start", "0");							
		param.put("limit", "10");
		
		param.put("data.age", logutil.mgIn(18,22,33,21));	//in查询
		//param.put("or",logutil.mgArray(logutil.mgkv("age",21),
		//				logutil.mgkv("data.mimi.ai", "htt")));	//逻辑or查询,
		
		NameValuePair[] params=new NameValuePair[]{
				new NameValuePair("params",param.toString())
		};
		
		
		
		
		
		//result		
		try {
			String resText = HttpUtils.getInstance().httpPost(HOST, params, HttpUtils.CHARSET_UTF8);
			JSONObject res=JSONObject.fromObject(resText);
			List<JSONObject> list =res.getJSONArray("records"); 
			for(int i=0;i<list.size();i++){
				JSONObject j = (JSONObject)JSONSerializer.toJSON(list.get(i));
				System.out.println(j);
			}
			long endTime = System.currentTimeMillis();
			System.out.println("时间:"+(endTime-startTime));
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
