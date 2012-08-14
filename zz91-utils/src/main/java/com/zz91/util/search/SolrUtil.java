/**
 * Copyright 2011 ASTO.
 * All right reserved.
 * Created on 2012-01-05
 */
package com.zz91.util.search;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.StreamingUpdateSolrServer;

import com.zz91.util.file.FileUtils;
import com.zz91.util.http.HttpUtils;
import com.zz91.util.lang.StringUtils;
import com.zz91.util.seo.SeoUtil;

/**
 * @author mays (mays@asto.com.cn)
 * 
 *         created on 2012-08-10
 */
public class SolrUtil {
	
	private static Logger LOG = Logger.getLogger(SeoUtil.class);
	
	private String DEFAULT_PROP = "search.properties";
	
	private String url="http://192.168.2.178:8080/solr";
	// 连接超时
	private Integer soTimeout = 10000;
	private Integer connectionTimeout = 10000;
//	// 每个主机的默认最大连接数
//	private Integer defaultMaxConnectionsPerHost = 100;
//	// 最大连接
//	private Integer maxTotalConnections = 100;
//	// 最大重试
//	private Integer maxRetries = 0;
	
	private static SolrUtil _instance;
	
//	private final static Map<String, CommonsHttpSolrServer> SOLR_SERVER=new HashMap<String, CommonsHttpSolrServer>();
	
	
	
	public static synchronized SolrUtil getInstance(){
		if(_instance==null){
			_instance =new SolrUtil();
		}
		return _instance;
	}
	
	public void init(){
		init(DEFAULT_PROP);
	}
	
	@SuppressWarnings("unchecked")
	public void init(String properties) {
		// 从配置文件读取搜索参数信息
		LOG.debug("Loading search properties:" + properties);
		
		try {
			
			Map<String, Object> map = FileUtils.readPropertyFile(properties, HttpUtils.CHARSET_UTF8);
			this.url=(String) map.get("search.url");
			this.soTimeout = Integer.valueOf(map.get("search.soTimeout").toString());
			this.connectionTimeout = Integer.valueOf(map.get("search.connectionTimeout").toString());
//			this.defaultMaxConnectionsPerHost =  Integer.valueOf(map.get("search.defaultMaxConnectionsPerHost").toString());
//			this.maxTotalConnections = Integer.valueOf(map.get("search.maxTotalConnections").toString());
//			this.maxRetries =  Integer.valueOf(map.get("search.maxRetries").toString());
			LOG.debug("Finish loading search properties:" + properties);
		} catch (IOException e) {
//			LOG.debug("Error loading search properties:" + StacktraceUtil.getStackTrace(e));
		}
		
	}
	
	public SolrServer getSolrServer() {
		return getSolrServer(null);
	}
	
	public SolrServer getSolrServer(String model) {
		
		if(StringUtils.isEmpty(model)){
			model="";
		}
		
		model=model.replace("\\/", "");
		
		StreamingUpdateSolrServer server = null;
		try {
			server = new StreamingUpdateSolrServer(url+"/"+model, 100, 3);
			
			server.setSoTimeout(soTimeout);
	        // 连接超时
	        server.setConnectionTimeout(connectionTimeout);
//	        // 每个主机的默认最大连接数
//	        server.setDefaultMaxConnectionsPerHost(defaultMaxConnectionsPerHost);
//	        // 最大连接
//	        server.setMaxTotalConnections(maxTotalConnections);
//	        // 最大重试
//	        server.setMaxRetries(maxRetries);
//	        // 跟踪重定向
//	        server.setFollowRedirects(false);
//	        // 允许压缩
//	        server.setAllowCompression(true);
//	        // 提升性能采用流输出方式
//	        server.setRequestWriter(new BinaryRequestWriter());
	        
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		return server;
	}
	
	/**
	 * 创建查询条件
	 * @param keyword 关键字（用于主查询）
	 * @param filterMap 需要过滤的字段（区间查询：query.addFilterQuery("Age:[* TO 50]");）
	 * @param sortMap 排序（排序字段）Map<字段，asc/desc>
	 * @param hight 是否支持高亮
	 * @param highlightField 需要高亮的字段
	 * @param start 分页开始
	 * @param limit 分页每页限制
	 * @return
	 */
	public static SolrQuery buildSolrQuery(String keyword, Map<String,Object> filterMap, Map<String,Object> sortMap,
			boolean hight, String[] highlightField, int start, int limit){
		SolrQuery query = new SolrQuery();
        //主查询
		if (StringUtils.isNotEmpty(keyword)) {
			query.setQuery(keyword);
		} else {
			query.setQuery("*:*");
		}
        //设置高亮
        if (hight) {
            query.setHighlight(true);
            for (String field:highlightField) {
            	 query.addHighlightField(field); 
			}
            query.setHighlightSimplePre("<em>"); 
            query.setHighlightSimplePost("</em>");
		}
        //采用过滤器查询可以提高性能
        if (filterMap != null) {
            for(String filtField : filterMap.keySet()) {
            	query.addFilterQuery(filtField+":"+filterMap.get(filtField)); 
            }
		} 
//      添加排序 默认按评分排序
        if (sortMap != null) {
            for(String sortField : sortMap.keySet()) {
            	if ("desc".equals(sortMap.get(sortField))) {
            		query.addSortField(sortField, ORDER.desc);
				} else {
					query.addSortField(sortField, ORDER.asc);
				}
            }
		} else {
			query.addSortField("score", ORDER.desc);
		}
        //分页返回结果
        query.setStart(start);
        query.setRows(limit);
        return query;
	}
}