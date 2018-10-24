package com.Lee.e3ItemWeb.message;

import com.Lee.e3ItemWeb.pojo.Item;
import com.Lee.pojo.TbItem;
import com.Lee.pojo.TbItemDesc;
import com.Lee.service.ItemService;
import com.alibaba.dubbo.config.annotation.Reference;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 监听后台添加商品发的信息(内容是商品id)，生成${id}.html静态页面文件
 */
@Component
public class HtmlGenerateListener {

    @Reference
    private ItemService itemService;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Value("${HTML_GENERATE_PATH}")
    private String HTML_GENERATE_PATH;

    // 使用JmsListener配置消费者监听的队列，其中入参text是接收到的消息
    @JmsListener(destination = "addItem.topic")  //发消息方的名称作为此监听者的destination
    public void onMessage(String message) {
        try {
            //创建一个ftl模板文件，参考jsp
            //从消息中取商品id
            Long itemId = new Long(message);
            //等待添加到数据库的事务提交
            Thread.sleep(100);
            //根据商品id查询商品信息，商品基本信息和商品描述。
            TbItem tbItem = itemService.getItemById(itemId);
            Item item = new Item(tbItem);
            //取商品描述
            TbItemDesc itemDesc = itemService.getItemDescById(itemId);
            //创建一个数据集，把商品数据封装
            Map dataMap = new HashMap<>();
            dataMap.put("item", item);
            dataMap.put("itemDesc", itemDesc);
            //加载已有的ftl模板对象
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("item.ftl");
            //创建一个输出流，指定输出的目录及文件名: ${itemId}.html
            FileWriter fileWriter = new FileWriter(HTML_GENERATE_PATH + "/" + itemId + ".html");
            //生成静态页面。
            template.process(dataMap,fileWriter);
            //关闭流
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
