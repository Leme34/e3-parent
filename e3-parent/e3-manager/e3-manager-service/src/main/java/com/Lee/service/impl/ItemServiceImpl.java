package com.Lee.service.impl;

import com.Lee.dao.TbItemDescMapper;
import com.Lee.dao.TbItemMapper;
import com.Lee.dto.E3Result;
import com.Lee.dto.EasyUIDataGridResultDTO;
import com.Lee.jedis.JedisClient;
import com.Lee.pojo.TbItem;
import com.Lee.pojo.TbItemDesc;
import com.Lee.pojo.TbItemExample;
import com.Lee.service.ItemService;
import com.Lee.utils.IDUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.jms.Topic;
import java.util.Date;
import java.util.List;

/**
 * 商品管理ServiceImpl
 * 由serviceImpl的dubbo的@service注解发布服务,在e3-web的Controller消费服务
 * 使用redis缓存，并设置缓存过期时间(防止冷门商品占用内存)
 */
@Component
@Service  //dubbo的@service注解发布服务
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    private Topic topic;
    @Autowired
    private JedisClient jedisClient;

    private Gson gson = new Gson();

    @Value("${ITEM_CACHE_EXPIRE}")
    private Integer ITEM_CACHE_EXPIRE;
    @Value("${REDIS_ITEM_PRE}")
    private String REDIS_ITEM_PRE;


    @Override
    public TbItem getItemById(long itemId) {
        //先查询缓存
        try {
            String json = jedisClient.get(ITEM_CACHE_EXPIRE + ":" + itemId + ":BASE");
            if (!StringUtils.isEmpty(json)){
                TbItem tbItem = gson.fromJson(json, TbItem.class);
                return tbItem;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //缓存没有，根据主键查询数据库
        //TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        //设置查询条件
        criteria.andIdEqualTo(itemId);
        //执行查询
        List<TbItem> list = itemMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            //添加缓存
            try {
                //使用前后缀防止key冲突,BASE后缀表示
                jedisClient.set(REDIS_ITEM_PRE +":" + itemId + ":BASE", gson.toJson(list.get(0)));
                //给此缓存设置过期时间
                jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":BASE", ITEM_CACHE_EXPIRE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list.get(0);
        }
        //查询结果为null
        return null;
    }

    @Override
    public EasyUIDataGridResultDTO getItemList(int page, int rows) {
        //设置分页信息
        PageHelper.startPage(page, rows);
        //无条件查询(查询所有)
        TbItemExample tbItemExample = new TbItemExample();
        List<TbItem> tbItems = itemMapper.selectByExample(tbItemExample);
        //取分页结果
        PageInfo<TbItem> tbItemPageInfo = new PageInfo<>(tbItems);
        //封装dto对象并返回页面
        EasyUIDataGridResultDTO resultDTO = new EasyUIDataGridResultDTO();
        resultDTO.setRows(tbItems);
        resultDTO.setTotal(tbItemPageInfo.getTotal());

        return resultDTO;
    }

    @Override
    public E3Result addItem(TbItem item, String desc) {
        //生成商品id
        long itemId = IDUtils.genItemId();
        //补全item的属性
        item.setId(itemId);
        //1-正常，2-下架，3-删除
        item.setStatus((byte) 1);
        item.setCreated(new Date());
        item.setUpdated(new Date());
        //向商品表插入数据
        itemMapper.insert(item);
        //创建一个商品描述表对应的pojo对象。
        TbItemDesc itemDesc = new TbItemDesc();
        //补全属性
        itemDesc.setItemId(itemId);
        itemDesc.setItemDesc(desc);
        itemDesc.setCreated(new Date());
        itemDesc.setUpdated(new Date());
        //向商品描述表插入数据
        tbItemDescMapper.insert(itemDesc);
        //发送消息，在e3-search-service工程中监听消息，查询数据库得到此商品信息dto对象索引到solr集群
        jmsMessagingTemplate.convertAndSend(topic, itemId + "");
        System.out.println("发送消息后=========");
        //删除以前的缓存实现缓存同步
        jedisClient.del(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
        //返回成功
        return E3Result.ok();
    }

    @Override
    public TbItemDesc getItemDescById(Long itemId) {
        //查询缓存
        try {
            String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
            if(!StringUtils.isEmpty(json)) {
                TbItemDesc tbItemDesc = gson.fromJson(json, TbItemDesc.class);
                return tbItemDesc;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //查询数据库
        //此方法能查出大文本
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);

        //添加缓存
        try {
            jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":DESC", gson.toJson(tbItemDesc));
            //设置过期时间
            jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":DESC", ITEM_CACHE_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tbItemDesc;
    }
}
