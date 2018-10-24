package com.Lee.service.impl;

import com.Lee.dao.TbContentMapper;
import com.Lee.dto.E3Result;
import com.Lee.dto.EasyUIDataGridResultDTO;
import com.Lee.jedis.JedisClient;
import com.Lee.pojo.TbContent;
import com.Lee.pojo.TbContentExample;
import com.Lee.service.ContentService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 商城主页内容管理Service，使用redis集群进行缓存
 */
@Component
@Service
public class ContentServiceImpl implements ContentService {

	//使用jedis集群版
	@Autowired
	private JedisClient jedisClient;

	@Autowired
	private TbContentMapper contentMapper;

	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;

	//后台增加内容要进行缓存同步（只需要删除此哈希表中以cid为key的缓存，下次就会去数据库查询并缓存）
	@Override
	public E3Result addContent(TbContent content) {
		//将内容数据插入到内容表
		content.setCreated(new Date());
		content.setUpdated(new Date());
		//插入到数据库
		contentMapper.insert(content);
		//删除以前的缓存实现缓存同步
		jedisClient.hdel(CONTENT_LIST,content.getCategoryId()+"");
		return E3Result.ok();
	}

	/**
	 * 根据内容分类id查询内容列表,使用redis缓存查询结果
	 */
	@Override
	public List<TbContent> getContentListByCid(long cid) {
		Gson gson = new Gson();

		//先查询缓存 ,捕获异常防止因为缓存问题导致事务失败
		try {
			String jsonResult = jedisClient.hget(CONTENT_LIST, cid + "");
			if(!StringUtils.isEmpty(jsonResult)){
				List cacheResult = gson.fromJson(jsonResult,
						new TypeToken<List<TbContent>>() {}.getType());
				System.out.println("从缓存取得");
				return cacheResult;
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		//缓存为空,查询数据库
		TbContentExample example = new TbContentExample();
		TbContentExample.Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andCategoryIdEqualTo(cid);
		//执行查询
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		System.out.println("查询数据库");

		//添加到缓存 ,捕获异常防止因为缓存问题导致事务失败
		try {
			jedisClient.hset(CONTENT_LIST,cid+"",gson.toJson(list));
		}catch (Exception e){
			e.printStackTrace();
		}


		return list;
	}

	@Override
	public EasyUIDataGridResultDTO getContentListPageByCid(long cid, int page, int rows) {
		//设置分页信息
		PageHelper.startPage(page,rows);
		TbContentExample example = new TbContentExample();
		TbContentExample.Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andCategoryIdEqualTo(cid);
		//执行查询
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		//取分页结果
		PageInfo<TbContent> tbContentPageInfo = new PageInfo<>(list);
		//封装dto对象并返回页面
		EasyUIDataGridResultDTO resultDTO = new EasyUIDataGridResultDTO();
		resultDTO.setTotal(tbContentPageInfo.getTotal());
		resultDTO.setRows(list);

		return resultDTO;
	}

	@Override
	public void deleteById(long id) {
		contentMapper.deleteByPrimaryKey(id);
		//删除以前的缓存实现缓存同步
		jedisClient.hdel(CONTENT_LIST,id+"");
	}

}
