package com.Lee.e3web.controller;

import com.Lee.dto.E3Result;
import com.Lee.e3mall.search.service.SearchItemService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 一键导入商品数据到索引库的Controller
 */
@Controller
public class SearchItemController {

    @Reference
    private SearchItemService searchItemService;

    @RequestMapping("/index/item/import")
    @ResponseBody
    public E3Result importItemList() {
        E3Result e3Result = searchItemService.importAllItems();
        return e3Result;
    }


}
