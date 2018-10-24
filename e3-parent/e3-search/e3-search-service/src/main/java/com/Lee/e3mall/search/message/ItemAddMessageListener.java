package com.Lee.e3mall.search.message;

import com.Lee.dao.ItemMapper;
import com.Lee.dto.SearchItemDTO;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 监听新增商品消息，接收到消息后，将此条商品信息同步到索引库
 */
@Component
public class ItemAddMessageListener {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrServer solrServer;

    // 使用JmsListener配置消费者监听的队列，其中入参text是接收到的消息
    @JmsListener(destination = "addItem.topic")  //发消息方的名称作为此监听者的destination
    public void onMessage(String message) {
        System.out.println("Consumer收到的报文为:" + message);
        try {
            //等待，保证发送方事务已提交到数据库
            Thread.sleep(100);
            //取出消息中的tbItem的id
            Long itemId = new Long(message);
            //根据商品id查询此商品信息
            SearchItemDTO searchItemDTO = itemMapper.getItemById(itemId);
            //创建一个索引文对象，并添加它的域信息
            SolrInputDocument document = new SolrInputDocument();
            document.addField("id", searchItemDTO.getId());
            document.addField("item_title", searchItemDTO.getTitle());
            document.addField("item_sell_point", searchItemDTO.getSell_point());
            document.addField("item_price", searchItemDTO.getPrice());
            document.addField("item_image", searchItemDTO.getImage());
            document.addField("item_category_name", searchItemDTO.getCategory_name());
            //把文档写入索引库并提交
            solrServer.add(document);
            solrServer.commit();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
