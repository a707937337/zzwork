/**
 * Copyright 2011 ASTO.
 * All right reserved.
 * Created on 2012-01-05
 */
package com.zz91.util.search.solr;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.http.client.HttpClient;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
//import org.apache.solr.client.solrj.impl.StreamingUpdateSolrServer;

/**
 * @author mays (mays@asto.com.cn)
 * 
 *         created on 2012-11-21
 */
public class SolrUpdateUtil {
	
	private static Logger LOG = Logger.getLogger(SolrUpdateUtil.class);
	
	private String DEFAULT_PROP = "search.properties";
	
	private String solrHost="http://192.168.3.30:8580/solr";
	
	private static SolrUpdateUtil _instance;
	
	private static Map<String, SolrServer> SOLR_SERVER = new HashMap<String, SolrServer>();
	static final int DEFAULT_QUEUE=100;
	static final int DEFAULT_THREAD=4;
	
	public static synchronized SolrUpdateUtil getInstance(){
		if(_instance==null){
			_instance =new SolrUpdateUtil();
		}
		return _instance;
	}
	
	public void init(){
		init(DEFAULT_PROP);
	}
	
	@SuppressWarnings("unchecked")
	public void init(String properties) {
		
		try {
			
			Map<String, Object> map = readPropertyFile(properties, "utf-8");
			this.solrHost=(String) map.get("search.url");
			LOG.debug("Finish loading search properties:" + properties);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public SolrServer getSolrServer(String coreName){
		return getSolrServer(coreName, null);
	}
	
	public SolrServer getSolrServer(String coreName, HttpClient httpclient){
		
		if(coreName==null || "".equals(coreName)) {
			return null;
		}
		
		if(!coreName.startsWith("/")){
			coreName = "/"+ coreName;
		}
		
		if(SOLR_SERVER.get(coreName)==null){
			SOLR_SERVER.put(coreName, new ConcurrentUpdateSolrServer(solrHost+coreName, httpclient, DEFAULT_QUEUE, DEFAULT_THREAD));
		}
		
		return SOLR_SERVER.get(coreName);
	}
	
	public void shutdown(String coreName){
		if(SOLR_SERVER.get(coreName)!=null){
			SOLR_SERVER.get(coreName).shutdown();
		}
	}
	
	public void shutdown(){
		for(String core: SOLR_SERVER.keySet()){
			SOLR_SERVER.get(core).shutdown();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap readPropertyFile(String file,String charsetName) throws IOException {
		if (charsetName==null || charsetName.trim().length()==0){
			charsetName="gbk";
		}
		HashMap map = new HashMap();
		InputStream is = SolrUpdateUtil.class.getClassLoader().getResourceAsStream(file);
		Properties properties = new Properties();
		properties.load(is);
		Enumeration en = properties.propertyNames();
		while (en.hasMoreElements()) {
			String key = (String) en.nextElement();
			String code = new String(properties.getProperty(key).getBytes(
					"ISO-8859-1"), charsetName);
			map.put(key, code);
		}
		return map;
	}
}