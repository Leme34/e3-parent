package com.Lee.e3mall.search.service.impl;

import com.Lee.dto.SearchResult;
import com.Lee.e3mall.search.dao.SearchDao;
import com.Lee.e3mall.search.service.SearchService;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 商品搜索Service
 */
@Component
@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao searchDao;

    /**
     *  主要是定义查询条件和setTotalPages
     */
    @Override
    public SearchResult search(String keyword, int page, int rows) throws Exception {
        SolrQuery query = new SolrQuery();
        //查询关键字,不管传入的关键字是否为空串，直接查询返回结果
        query.setQuery(keyword);
        //设置合法的分页条件
        if (page <= 0) page = 1;
        //以第page-1页的最后一条为开始
        query.setStart((page-1)*rows);
        query.setRows(rows);
        //设置默认搜索域
        query.set("df", "item_title");
        //开启高亮显示
        query.setHighlight(true);
        query.addHighlightField("item_title");
        //html标签前后缀
        query.setHighlightSimplePre("<em style=\"color:red\">");
        query.setHighlightSimplePost("</em>");
        //调用dao执行查询
        SearchResult searchResult = searchDao.search(query);
        //计算总页数
        long recordCount = searchResult.getRecordCount();
        int totalPage = (int) (recordCount / rows);
        //若有余数则增加一页显示
        if (recordCount % rows > 0) totalPage ++;
        //添加到返回结果dto
        searchResult.setTotalPages(totalPage);
        //返回结果
        return searchResult;
    }
}
