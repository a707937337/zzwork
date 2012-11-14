package src.com.zz91;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

import com.zz91.util.search.SorlUtil;

public class SortTest {
	
	public static void main(String[] args) throws SolrServerException {
		SorlUtil.getInstance().init("search.properties");
		SolrServer server = SorlUtil.getInstance().getSolrServer();
		String hig[] = {"name","detailsQuery"};
		SolrQuery query = SorlUtil.buildSolrQuery("污水", null, null, true, hig, 0, 2);
		QueryResponse rsp = server.query(query);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        List<CompanySearchDto> beans = rsp.getBeans(CompanySearchDto.class);
        Map<String,Map<String,List<String>>> hl = rsp.getHighlighting();
        Map<String,Object> _map ;
        for (CompanySearchDto u : beans) {
        	_map = new HashMap<String,Object>();
        	_map.put("id", u.getId());
        	if (hl.get(u.getId().toString()).get("name") != null) {
        		_map.put("name", hl.get(u.getId().toString()).get("name").get(0));
			} else {
				_map.put("name", u.getName());
			}
        	if (hl.get(u.getId().toString()).get("detailsQuery") != null) {
        		_map.put("detailsQuery", hl.get(u.getId().toString()).get("detailsQuery").get(0));
			} else {
				_map.put("detailsQuery", u.getDetailsQuery());
			}
        	_map.put("address", u.getAddress());
        	_map.put("indeustryName", u.getIndeustryName());
        	_map.put("businessName", u.getBusinessName());
        	_map.put("provinceName", u.getProvinceName());
        	_map.put("areaName", u.getAreaName());
        	list.add(_map);
        }
        //输出符合条件的结果数
        System.out.println("NumFound: " + rsp.getResults().getNumFound());
        //输出结果
        for (Map<String,Object> map : list) {
        	for(Map.Entry<String, Object> entry : map.entrySet())   
        	{   
        	    System.out.println(entry.getKey()+": "+entry.getValue());   
        	}
		}
	}
}
