package com.Lee.e3searchservice.conf;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注入SolrServer对象到ioc容器
 */
@Configuration
public class SolrConfig {

    /**
     * 配置文件注入单机版solr服务的url
     */
//    @Value("${SOLR_URL}")
//    private String SOLR_URL;


    /**
     * 使用solr集群则用","分隔每个url
     */
    @Value("${zkHosts}")
    private String zkHosts;

    /**
     *  使用solr集群必须指定默认collection
     */
    @Value("${DEFAULT_COLLECTION}")
    private String DEFAULT_COLLECTION;

    /**
     * 注入单机版solrServer
     */
//    @Bean
//    public HttpSolrServer solrServer(){
//        System.out.println("注入单机版solrServer，SOLR_URL="+SOLR_URL);
//        //solr服务的url
//        return new HttpSolrServer(SOLR_URL);
//    }

    /**
     * 注入集群版solrServer
     */
    @Bean
    public SolrServer solrServer(){
        System.out.println("注入集群版solrServer，zkHosts="+zkHosts);
        CloudSolrServer cloudSolrServer = new CloudSolrServer(zkHosts);
        cloudSolrServer.setDefaultCollection(DEFAULT_COLLECTION);
        return cloudSolrServer;
    }


}
