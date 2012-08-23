/**
 * Copyright 2011 ASTO.
 * All right reserved.
 * Created on 2011-9-15
 */
package com.zz91.util.auth.ep;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpException;
import com.zz91.util.encrypt.MD5;
import com.zz91.util.exception.AuthorizeException;
import com.zz91.util.http.HttpUtils;
import com.zz91.util.http.SessionUtils;
import com.zz91.util.lang.StringUtils;

/**
 * @author Leon
 * 
 *         created on 2011-9-15
 */
public class EpAuthUtils extends SessionUtils{

	private static EpAuthUtils _instance = null;
	private static final String URLPREFIX = EpAuthConst.API_HOST; // URL前缀
	
	private EpAuthUtils() {

	}

	synchronized public static EpAuthUtils getInstance() {
		if (_instance == null) {
			_instance = new EpAuthUtils();
		}
		return _instance;
	}


	/**
	 * 发送消息包含：account，password，projectCode
	 * ticket=account+password+projectCode+projectPassword+key
	 * 返回信息包含：epAuthUser，ticket，key
	 * @throws IOException 
	 * @throws AuthorizeException 
	 * @throws NoSuchAlgorithmException 
	 * @throws UnsupportedEncodingException 
	 */
	public EpAuthUser validateUser(HttpServletResponse response,
			String account, String password, String ip) throws IOException, AuthorizeException, NoSuchAlgorithmException{
		String encodePwd=null;
		try {
			encodePwd = MD5.encode(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return validateUserByEncodePwd(response, account, encodePwd, ip);
	}
	public EpAuthUser validateUserByEncodePwd(HttpServletResponse response,
			String account, String password,String ip) throws IOException, AuthorizeException, NoSuchAlgorithmException {
		EpAuthUser epAuthUser =null;
		String encodeAccount = URLEncoder.encode(account, "utf-8");
		String result = HttpUtils.getInstance().httpGet(URLPREFIX+"/validationUser.htm?a="+encodeAccount+"&p="+password+"&ip="+ip+"&project="+EpAuthConst.PROJECT,HttpUtils.CHARSET_UTF8);
		JSONObject resultJson = JSONObject.fromObject(result);
		if(resultJson.containsKey("error") && !StringUtils.isEmpty(resultJson.getString("error"))){
			throw new AuthorizeException(resultJson.getString("error"));
		}
		if(!resultJson.containsKey("key") || !resultJson.containsKey("ticket") || !resultJson.containsKey("epAuthUser")){
			throw new AuthorizeException(AuthorizeException.ERROR_SERVER);
		}
		String key=resultJson.getString("key");
		String ticket=resultJson.getString("ticket");
		if(StringUtils.isEmpty(key) || StringUtils.isEmpty(ticket)){
			throw new AuthorizeException(AuthorizeException.ERROR_SERVER);
		}
		
		String validateTicket=MD5.encode(account+password+EpAuthConst.PROJECT+key);
		if(!ticket.equals(validateTicket)){
			throw new AuthorizeException(AuthorizeException.ERROR_SERVER);
		}
		
		if(!resultJson.getString("epAuthUser").equals("null")){
			epAuthUser = (EpAuthUser) JSONObject.toBean(resultJson.getJSONObject("epAuthUser"), EpAuthUser.class);
		}
		
		if(epAuthUser==null){
			throw new AuthorizeException(AuthorizeException.ERROR_SERVER);
		}
		HttpUtils.getInstance().setCookie(response, EpAuthConst.TICKET_KEY,ticket, EpAuthConst.EP_DOMAIN, null);

		return epAuthUser;
	}
	
	
	/**
	 * 发送信息包含：ticket，projectCode
	 * vticket=projectCode+projectPassword+key
	 * 返回信息包含：EpAuthUser，vticket，key
	 */
	public EpAuthUser validateTicket(HttpServletRequest request){
		EpAuthUser epAuthUser = null;
		String ticket = HttpUtils.getInstance().getCookie(request, EpAuthConst.TICKET_KEY, EpAuthConst.EP_DOMAIN);
		String result = null;
		try {
			result = HttpUtils.getInstance().httpGet(URLPREFIX + "/validationTicket.htm?t=" + ticket+"&project="+EpAuthConst.PROJECT, HttpUtils.CHARSET_UTF8);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(result==null||"".equals(result)){
			return null;
		}
		JSONObject resultJson = JSONObject.fromObject(result);
		if(!resultJson.containsKey("key") || !resultJson.containsKey("vticket")){
			return null;
		}
		
		String key = resultJson.getString("key");
		String vticket = resultJson.getString("vticket");
		
		if(StringUtils.isEmpty(key) || StringUtils.isEmpty(vticket)){
			return null;
		}
		String velidateTicket=null;
		try {
			velidateTicket = MD5.encode(key+ticket);
		} catch (NoSuchAlgorithmException e) {
		} catch (UnsupportedEncodingException e) {
		}
		
		if(!velidateTicket.equals(vticket)){
			return null;
		}
		if(!resultJson.getString("epAuthUser").equals("null")){
			epAuthUser = (EpAuthUser) JSONObject.toBean(resultJson.getJSONObject("epAuthUser"), EpAuthUser.class);
		}
	
		return epAuthUser;
	}
	public void logout(HttpServletRequest request,
			HttpServletResponse response, String sessionid) {
		// 得到票据，重置cookie
		String ticket = HttpUtils.getInstance().getCookie(request,
				EpAuthConst.TICKET_KEY, EpAuthConst.EP_DOMAIN);
		try {
			HttpUtils.getInstance().httpGet(URLPREFIX + "/epAuthLogout.htm?t=" + ticket, HttpUtils.CHARSET_UTF8);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		clearnEpAuthUser(request, sessionid);
		HttpUtils.getInstance().setCookie(response, EpAuthConst.TICKET_KEY, null, EpAuthConst.EP_DOMAIN, 0);
	}
	
	public boolean authorizeRight(String rightContent, HttpServletRequest request, String sessionid){
		if(StringUtils.isEmpty(rightContent)){
			return false;
		}
		
		EpAuthUser sessionUser = getEpAuthUser(request, sessionid);
		String[] rightArr=sessionUser.getRightList();
		if(rightArr==null||"".equals(rightArr)){
			return false;
		}
		
		for(String s:rightArr){
			if(rightContent.equals(s)){
				return true;
			}
		}
		return false;
	}

	public EpAuthUser getEpAuthUser(HttpServletRequest request,
			String sessionid) {
		String tickkey = HttpUtils.getInstance().getCookie(request,
				EpAuthConst.TICKET_KEY, EpAuthConst.EP_DOMAIN);
		if (StringUtils.isEmpty(tickkey)) {
			clearnEpAuthUser(request, sessionid);
			return null;
		}
		EpAuthUser epAuthUser = null;
		if (sessionid == null||"".equals(sessionid)) {
			// TODO 使用session实现
			epAuthUser = (EpAuthUser) request.getSession().getAttribute(
					EpAuthUser.SESSION_KEY);
		} else {
			// TODO 使用memcached实现
		}
		return epAuthUser;
	}

	public void setEpAuthUser(HttpServletRequest request, EpAuthUser epAuthUser,
			String sessionid) {
		if (sessionid == null||"".equals(sessionid)) {
			// TODO 使用session实现
			request.getSession().setAttribute(EpAuthUser.SESSION_KEY, epAuthUser);
		} else {
			// TODO 使用memcached实现
		}
	}

	public void clearnEpAuthUser(HttpServletRequest request, String sessionid) {
		if (sessionid == null||"".equals(sessionid)) {
			// TODO 使用session实现
			request.getSession().removeAttribute(EpAuthUser.SESSION_KEY);
		} else {
			// TODO 使用memcached实现
		}
	}
	/**
	public static void main(String[] args) {
		EpAuthConst.PROJECT="myesite";
		EpAuthConst.API_HOST="http://localhost:880/admin/api";
		EpAuthConst.EP_DOMAIN="huanbao.com";
		EpAuthConst.PROJECT_PASSWORD="135246";
	}
	*/
	public String getSID(){
		return EpAuthUser.SESSION_KEY;
	}
	
	public String getDomain(){
		return EpAuthConst.EP_DOMAIN;
	}
	
	public Integer getSessionAge() {
		return 69*60;
	}
}
