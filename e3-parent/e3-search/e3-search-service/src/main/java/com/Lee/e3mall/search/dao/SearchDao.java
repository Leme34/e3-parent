package com.Lee.e3mall.search.dao;

import com.Lee.dto.SearchItemDTO;
import com.Lee.dto.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品搜索dao
 */
@Repository
public class SearchDao {

    @Autowired
    private SolrServer solrServer;

    /**
     * 根据service层传入的查询条件查询索引库返回结果dto
     */
    public SearchResult search(SolrQuery query) throws Exception {
        SearchResult searchResult = new SearchResult();

        // 执行查询索引库
        QueryResponse queryResponse = solrServer.query(query);
        // 取得查询结果
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        // 取得总记录数
        searchResult.setRecordCount(solrDocumentList.getNumFound());
        // 取得高亮显示结果的商品列表
        Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
        List<SearchItemDTO> itemList = new ArrayList<>();
        for (SolrDocument solrDocument : solrDocumentList) {
            SearchItemDTO item = new SearchItemDTO();
            //从solrDocument取普通显示的字段
            item.setId((String) solrDocument.get("id"));
            item.setCategory_name((String) solrDocument.get("item_category_name"));
            item.setImage((String) solrDocument.get("item_image"));
            item.setPrice((long) solrDocument.get("item_price"));
            item.setSell_point((String) solrDocument.get("item_sell_point"));
            //取高亮查询结果(根据solr后台高亮查询结果分析数据结构)
            List<String> highlightingTitles = highlighting.get(item.getId()).get("item_title");
            //若高亮结果不为空则set高亮标题,否则set普通标题
            if (highlightingTitles != null && highlighting.size() > 0) {
                item.setTitle(highlightingTitles.get(0));
            }else {
                item.setTitle((String) solrDocument.get("item_title"));
            }
            //添加到商品列表
            itemList.add(item);
        }
        //添加到返回结果dto
        searchResult.setItemList(itemList);
        //返回结果
        return  searchResult;
    }


}
