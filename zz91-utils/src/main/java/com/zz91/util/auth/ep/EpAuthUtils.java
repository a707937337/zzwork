/**
 * Copyright 2011 ASTO.
 * All right reserved.
 * Created on 2011-9-15
 */
package com.zz91.util.auth.ep;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.zz91.util.encrypt.MD5;
import com.zz91.util.http.HttpUtils;
import com.zz91.util.lang.StringUtils;

/**
 * @author Leon
 * 
 *         created on 2011-9-15
 */
public class EpAuthUtils {

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

	private final static int TIMEOUT = 100000; // TODO 发布后改为10秒＝10000

	/**
	 * 发送消息包含：account，password，projectCode
	 * ticket=account+password+projectCode+projectPassword+key
	 * 返回信息包含：epAuthUser，ticket，key
	 */
	public EpAuthUser validateUser(HttpServletResponse response,
			String account, String password, Integer expired) {
		EpAuthUser epAuthUser = null;
		try {
			String encodePwd = MD5.encode(password);
			// 提交给服务器验证
			URL url = new URL(URLPREFIX + "/epAuthUser.htm?a=" + account + "&p=" + encodePwd+"&project="+EpAuthConst.PROJECT);
			Document doc = Jsoup.parse(url, TIMEOUT);
			epAuthUser = new EpAuthUser();
			String epAuthUserObj=doc.select("body").text();
			if(!epAuthUserObj.startsWith("{")){
				return null;
			}
			/*epAuthUser=(EpAuthUser) JSONObject.toBean(JSONObject.fromObject(epAuthUserObj), EpAuthUser.class);
			JSONObject.fromObject(epAuthUserObj).get("cid");*/
			JSONObject json = JSONObject.fromObject(epAuthUserObj);
			epAuthUser.setUid((Integer)(json.get("uid")));
			epAuthUser.setCid((Integer)(json.get("cid")));
			epAuthUser.setMemberCode(json.get("memberCode").toString());
			epAuthUser.setAccount(json.get("account").toString());
			epAuthUser.setLoginName(json.get("loginName").toString());
			String rights = json.get("rightList").toString();
			rights=rights.replace("[", "").replace("]", "").replace(" ", "").replace("\"", "");
			epAuthUser.setRightList(rights.split(","));
			
			epAuthUser.setKey(json.get("key").toString());
			epAuthUser.setTicket(json.get("ticket").toString());

			HttpUtils.getInstance().setCookie(response, EpAuthConst.TICKET_KEY,
					epAuthUser.getTicket(), EpAuthConst.EP_DOMAIN, null);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return epAuthUser;
	}
	
	
	/**
	 * 发送信息包含：ticket，projectCode
	 * vticket=projectCode+projectPassword+key
	 * 返回信息包含：EpAuthUser，vticket，key
	 */
	public EpAuthUser validateTicket(HttpServletRequest request){
		URL url;
		EpAuthUser epAuthUser = null;
		String ticket=HttpUtils.getInstance().getCookie(request, EpAuthConst.TICKET_KEY, EpAuthConst.EP_DOMAIN);
		if(StringUtils.isEmpty(ticket)){
			return null;
		}
		try {
			url = new URL(URLPREFIX + "/epAuthTicket.htm?t=" + ticket+"&project="+EpAuthConst.PROJECT);
			Document doc = Jsoup.parse(url, TIMEOUT);
			
			String epAuthUserObj=doc.select("body").text();
			if(!epAuthUserObj.startsWith("{")){
				return null;
			}
			
			JSONObject json = JSONObject.fromObject(epAuthUserObj);
//			String key=json.get("key").toString();
//			if(StringUtils.isEmpty(key)){
//				return null;
//			}
			
			epAuthUser = new EpAuthUser();
			
			epAuthUser.setUid((Integer)(json.get("uid")));
			epAuthUser.setCid((Integer)(json.get("cid")));
			epAuthUser.setMemberCode(json.get("memberCode").toString());
			epAuthUser.setAccount(json.get("account").toString());
			epAuthUser.setLoginName(json.get("loginName").toString());
			epAuthUser.setTicket(ticket);
			String rights = json.get("rightList").toString();
			rights=rights.replace("[", "").replace("]", "").replace(" ", "").replace("\"", "");
			epAuthUser.setRightList(rights.split(","));

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return epAuthUser;
	}
	public void logout(HttpServletRequest request,
			HttpServletResponse response, String sessionid) {
		// 得到票据，重置cookie
		String ticket = HttpUtils.getInstance().getCookie(request,
				EpAuthConst.TICKET_KEY, EpAuthConst.EP_DOMAIN);
		URL url;
		try {
			url = new URL(URLPREFIX + "/epAuthLogout.htm?t=" + ticket);
			Jsoup.parse(url, TIMEOUT);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		clearnEpAuthUser(request, sessionid);
		HttpUtils.getInstance().setCookie(response, EpAuthConst.TICKET_KEY,
				null, EpAuthConst.EP_DOMAIN, 0);
	}
	
	public boolean authorizeRight(String rightContent, HttpServletRequest request, String sessionid){
		if(StringUtils.isEmpty(rightContent)){
			return false;
		}
		
		EpAuthUser sessionUser = getEpAuthUser(request, sessionid);
		String[] rightArr=sessionUser.getRightList();
		if(rightArr==null){
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
		if (sessionid == null) {
			// TODO 使用session实现
			epAuthUser = (EpAuthUser) request.getSession().getAttribute(
					EpAuthUser.SESSION_KEY);
		} else {
			// TODO 使用memcached实现
		}
		return epAuthUser;
	}

	public void setEpAuthUser(HttpServletRequest request, EpAuthUser ssoUser,
			String sessionid) {
		if (sessionid == null) {
			// TODO 使用session实现
			request.getSession().setAttribute(EpAuthUser.SESSION_KEY, ssoUser);
		} else {
			// TODO 使用memcached实现
		}
	}

	public void clearnEpAuthUser(HttpServletRequest request, String sessionid) {
		if (sessionid == null) {
			// TODO 使用session实现
			request.getSession().removeAttribute(EpAuthUser.SESSION_KEY);
		} else {
			// TODO 使用memcached实现
		}
	}
	
	public static void main(String[] args) {
		EpAuthConst.PROJECT="myesite";
		EpAuthConst.API_HOST="http://localhost:880/admin/api";
		EpAuthConst.EP_DOMAIN="huanbao.com";
		EpAuthConst.PROJECT_PASSWORD="135246";
	}
	
}
