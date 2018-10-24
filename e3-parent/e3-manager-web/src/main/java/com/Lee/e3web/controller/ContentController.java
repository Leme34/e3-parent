package com.Lee.e3web.controller;

import com.Lee.dto.E3Result;
import com.Lee.dto.EasyUIDataGridResultDTO;
import com.Lee.pojo.TbContent;
import com.Lee.service.ContentService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

/**
 * 内容管理Controller
 * content.jsp的js代码中发ajax请求
 */
@Controller
public class ContentController {

	@Reference
	private ContentService contentService;

	@PostMapping("/content/save")
	@ResponseBody
	public E3Result addContent(TbContent content) {
		//调用服务把内容数据保存到数据库
		E3Result e3Result = contentService.addContent(content);
		return e3Result;
	}

	@GetMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResultDTO list(Long categoryId, int page, int rows, ModelMap modelMap){
		EasyUIDataGridResultDTO resultDTO = contentService.getContentListPageByCid(categoryId, page, rows);
		return resultDTO;
	}

	@PostMapping("/content/delete")
	@ResponseBody
	public E3Result delete(String ids){
		String[] idString = ids.split(",");
		List<String> idList = Arrays.asList(idString);
		for (String id : idList){
			contentService.deleteById(Long.parseLong(id));
		}
		E3Result result = new E3Result();
		result.setStatus(200);
		return result;
	}
}
