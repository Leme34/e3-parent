package com.Lee.e3mall.search.service.impl;

import com.Lee.dto.E3Result;
import com.Lee.dto.SearchItemDTO;
import com.Lee.e3mall.search.service.SearchItemService;
import com.alibaba.dubbo.config.annotation.Service;
import com.Lee.dao.ItemMapper;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * 索引库维护Service
 */
@Component
@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;
	
	@Override
	public E3Result importAllItems() {
		try {
			//查询所有商品
			List<SearchItemDTO> itemList = itemMapper.getItemList();
			//遍历商品列表,写入索引库
			for (SearchItemDTO searchItem : itemList) {
				//创建文档对象
				SolrInputDocument document = new SolrInputDocument();
				//向文档对象中添加域 ,对应配置文件中的每个stored="true"的<Field>
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategory_name());
				//把文档对象写入索引库
				solrServer.add(document);
			}
			//提交
			solrServer.commit();
			System.out.println("提交完成");
			//返回导入成功
			return E3Result.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return E3Result.build(500, "数据导入时发生异常");

		}
	}

}
