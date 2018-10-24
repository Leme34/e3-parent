package com.Lee.e3searchweb.controller;

import com.Lee.dto.SearchResult;
import com.Lee.e3mall.search.service.SearchService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 商品搜索Controller
 */
@Controller
public class SearchController {
	
	@Reference
	private SearchService searchService;
	
	@Value("${SEARCH_RESULT_ROWS}")
	private Integer SEARCH_RESULT_ROWS;

	@RequestMapping("/search")
	public String searchItemList(String keyword, 
			@RequestParam(defaultValue="1") Integer page, Model model) throws Exception {
		//keyword = new String(keyword.getBytes("iso-8859-1"), "utf-8");
		//查询商品列表
		SearchResult searchResult = searchService.search(keyword, page, SEARCH_RESULT_ROWS);
		//把结果传递给页面
		model.addAttribute("query", keyword);
		model.addAttribute("totalPages", searchResult.getTotalPages());
		model.addAttribute("page", page);
		model.addAttribute("recourdCount", searchResult.getRecordCount());
		model.addAttribute("itemList", searchResult.getItemList());

		//返回逻辑视图
		return "search";
	}
}
