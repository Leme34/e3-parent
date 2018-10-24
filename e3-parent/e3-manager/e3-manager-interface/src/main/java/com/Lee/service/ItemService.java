package com.Lee.service;

import com.Lee.dto.E3Result;
import com.Lee.dto.EasyUIDataGridResultDTO;
import com.Lee.pojo.TbItem;
import com.Lee.pojo.TbItemDesc;

public interface ItemService {
    TbItem getItemById(long itemId);

    EasyUIDataGridResultDTO getItemList(int page, int rows);

    E3Result addItem(TbItem item, String desc);

    //查询商品详细页信息
    TbItemDesc getItemDescById(Long itemId);
}
