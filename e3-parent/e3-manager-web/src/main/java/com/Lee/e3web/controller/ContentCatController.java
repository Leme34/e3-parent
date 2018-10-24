package com.Lee.e3web.controller;

import com.Lee.dto.E3Result;
import com.Lee.dto.EasyUITreeNodeDTO;
import com.Lee.service.ContentCategoryService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 内容分类管理Controller
 */
@Controller
public class ContentCatController {

	@Reference
	private ContentCategoryService contentCategoryService;

	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNodeDTO> getContentCatList(@RequestParam(name="id", defaultValue="0")Long parentId) {
		List<EasyUITreeNodeDTO> list = contentCategoryService.getContentCatList(parentId);
		return list;
	}

	/**
	 * 添加分类节点
	 */
	@RequestMapping(value="/content/category/create", method=RequestMethod.POST)
	@ResponseBody
	public E3Result createContentCategory(Long parentId, String name) {
		//调用服务添加节点
		E3Result e3Result = contentCategoryService.addContentCategory(parentId, name);
		return e3Result;
	}


}
