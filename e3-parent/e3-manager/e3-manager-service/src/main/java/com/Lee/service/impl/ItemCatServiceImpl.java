package com.Lee.service.impl;

import com.Lee.dao.TbItemCatMapper;
import com.Lee.dto.EasyUITreeNodeDTO;
import com.Lee.pojo.TbItemCat;
import com.Lee.pojo.TbItemCatExample;
import com.alibaba.dubbo.config.annotation.Service;
import com.Lee.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类管理
 */

@Service
@Component
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Override
	public List<EasyUITreeNodeDTO> getItemCatlist(long parentId) {
		//根据parentId查询子节点列表
		TbItemCatExample example = new TbItemCatExample();
		TbItemCatExample.Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		//创建返回结果List
		List<EasyUITreeNodeDTO> resultList = new ArrayList<>();
		//把列表转换成EasyUITreeNode列表
		for (TbItemCat tbItemCat : list) {
			EasyUITreeNodeDTO node = new EasyUITreeNodeDTO();
			//设置属性
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			//添加到结果列表
			resultList.add(node);
		}
		//返回结果
		return resultList;
	}

}
