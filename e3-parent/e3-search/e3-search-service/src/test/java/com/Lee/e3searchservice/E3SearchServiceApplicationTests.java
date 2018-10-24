package com.Lee.e3searchservice;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.Queue;
import javax.jms.Topic;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@MapperScan("com.Lee.dao")
@RunWith(SpringRunner.class)
@SpringBootTest
public class E3SearchServiceApplicationTests {

	@Autowired
	private JmsMessagingTemplate jms;

	@Autowired
	private Queue queue;

	@Autowired
	private Topic topic;

	@Test
	public void contextLoads() {
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void testMQ() {
		jms.convertAndSend(queue,"hello");
	}










	@Test
	public void addDocument() throws Exception {
		//创建一个SolrServer对象，创建一个连接。参数solr服务的url
		SolrServer solrServer = new HttpSolrServer("http://192.168.11.102:8080/solr/collection1");
		//创建一个文档对象SolrInputDocument
		SolrInputDocument document = new SolrInputDocument();
		//向文档对象中添加域。文档中必须包含一个id域，所有的域的名称必须在schema.xml中定义。
		document.addField("id", "doc01");
		document.addField("item_title", "测试商品01");
		document.addField("item_price", 1000);
		//把文档写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
	}

	@Test
	public void deleteDocument() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://192.168.11.102:8080/solr/collection1");
		//删除文档
		//solrServer.deleteById("doc01");
		solrServer.deleteByQuery("id:doc01");
		//提交
		solrServer.commit();
	}

	@Test
	public void queryIndex() throws Exception {
		//创建一个SolrServer对象。
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.163:8080/solr/collection1");
		//创建一个SolrQuery对象。
		SolrQuery query = new SolrQuery();
		//设置查询条件。
		//query.setQuery("*:*");
		query.set("q", "*:*");
		//执行查询，QueryResponse对象。
		QueryResponse queryResponse = solrServer.query(query);
		//取文档列表。取查询结果的总记录数
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println("查询结果总记录数：" + solrDocumentList.getNumFound());
		//遍历文档列表，从取域的内容。
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
		}
	}

	@Test
	public void queryIndexFuza() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.163:8080/solr/collection1");
		//创建一个查询对象
		SolrQuery query = new SolrQuery();
		//查询条件
		query.setQuery("手机");  //关键字
		query.setStart(0);//分页
		query.setRows(20);
		query.set("df", "item_title");//设置默认搜索域名
		query.setHighlight(true);   //开启高亮显示
		query.addHighlightField("item_title");  //高亮显示字段
		query.setHighlightSimplePre("<em>");  //高亮的html前缀
		query.setHighlightSimplePost("</em>");  //高亮的html后缀
		//执行查询
		QueryResponse queryResponse = solrServer.query(query);
		//取文档列表。取查询结果的总记录数
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println("查询结果总记录数：" + solrDocumentList.getNumFound());
		//遍历文档列表，从取域的内容。
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			//取高亮显示
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String title = "";
			//若高亮搜索结果不为空
			if (list !=null && list.size() > 0 ) {
				title = list.get(0);  //只会有一条数据
			} else {  //若高亮搜索结果为空，则显示普通搜索的结果
				title = (String) solrDocument.get("item_title");
			}
			System.out.println(title);
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
		}
	}

}
