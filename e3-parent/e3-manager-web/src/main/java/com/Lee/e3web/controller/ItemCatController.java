package com.Lee.e3web.controller;

import com.Lee.dto.EasyUITreeNodeDTO;
import com.alibaba.dubbo.config.annotation.Reference;
import com.Lee.service.ItemCatService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

/**
 * 商品分类管理Controller
 */
@Controller
public class ItemCatController {

	@Reference
	private ItemCatService itemCatService;
	
	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNodeDTO> getItemCatList(
			@RequestParam(name="id", defaultValue="0")Long parentId) {
		//调用服务查询节点列表
		List<EasyUITreeNodeDTO> list = itemCatService.getItemCatlist(parentId);
		return list;
		
	}
}
