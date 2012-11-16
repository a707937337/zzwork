package src.com.zz91;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

public class Solr4Test {
	
	public static final String SOLR_HOST="http://192.168.3.28:8580/solr";

	public static void main(String[] args) {
		Solr4Test t=new Solr4Test();
		for(int i=61;i<=80;i=i+20){
			t.testUpdate(i);
		}
		
//		t.testQuery();
	}
	
	public void testUpdate(int baseinfo){
		SolrServer solrServer = new ConcurrentUpdateSolrServer(SOLR_HOST+"/tradesupply", 100, 4);
		
		List<SolrInputDocument> list=new ArrayList<SolrInputDocument>();
		for(int i=baseinfo;i<(baseinfo+20);i++){
			SolrInputDocument document=new SolrInputDocument();
			document.addField("id", i);
			document.addField("title", "title"+i);
			document.addField("textcn", "textcn简单，浙江杭州，中国"+i);
			document.addField("textik", "浙江杭州，中国"+i);
			document.addField("kwSimple", "kwSimple简单的中国杭州"+i);
			document.addField("kwMaxWord", "30服务器kwMaxWord 混合 "+i);
			document.addField("kwComplex", "kwComplex 复杂 "+i);
			document.addField("kwInt", i);
			document.addField("kwLong", i*100l);
			document.addField("kwDouble", new Double("1."+i));
			document.addField("kwDate", new Date());
			document.addField("kwFloat", new Float("2."+i));
			document.addField("dynamic_i", i*100);
			document.addField("dynamic_s", "dynamic string "+i);
			
			list.add(document);
		}
		
		try {
			solrServer.add(list);
			solrServer.commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		solrServer.shutdown();
		
	}
	
	public void testQuery(){
		SolrServer solrServer = new HttpSolrServer(SOLR_HOST+"/tradesupply");
		
		SolrQuery query=new SolrQuery();
		query.setQuery("kwSimple:杭州");
		query.setHighlight(true);
		query.addHighlightField("kwSimple");
		query.setHighlightSimplePre("<em>");
		query.setHighlightSimplePost("</em>");
		try {
			QueryResponse rsp = solrServer.query(query);
			System.out.println(rsp.getResults().getNumFound());
			List<SolrDocument> doclist=rsp.getResults();
			if(doclist.size()>0){
				System.out.println(doclist.get(0).getFieldValue("title")+" "+doclist.get(0).getFieldValue("kwSimple"));
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		solrServer.shutdown();
	}
	
	public void testQueryRang(){
		
	}
	
	public void testDemo(){
		//SOLR 语法
	}
	
}
