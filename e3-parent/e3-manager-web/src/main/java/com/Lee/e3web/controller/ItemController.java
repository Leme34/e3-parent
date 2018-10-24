package com.Lee.e3web.controller;

import com.Lee.dto.E3Result;
import com.Lee.dto.EasyUIDataGridResultDTO;
import com.alibaba.dubbo.config.annotation.Reference;
import com.Lee.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.Lee.pojo.TbItem;

/**
 * 商品管理后台的Controller
 */
@Controller
public class ItemController {

    @Reference   //从注册中心获取已注册的服务
    private ItemService itemService;

    /**
     * 新增商品中的分类展示树
     */
    @RequestMapping("/item/{itemId}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long itemId) {
        TbItem tbItem = itemService.getItemById(itemId);
        return tbItem;
    }

    /**
     * 查询商品列表
     */
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResultDTO getItemList(Integer page, Integer rows){
        //查询商品列表
        EasyUIDataGridResultDTO dtoList = itemService.getItemList(page, rows);
        return dtoList;
    }

    /**
     * 商品添加功能
     */
    @RequestMapping(value="/item/save", method= RequestMethod.POST)
    @ResponseBody
    public E3Result addItem(TbItem item, String desc) {
        E3Result result = itemService.addItem(item, desc);
        return result;
    }


}
