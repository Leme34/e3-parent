package com.Lee.dao;

import com.Lee.dto.SearchItemDTO;
import java.util.List;

public interface ItemMapper {

    List<SearchItemDTO> getItemList();

    //接受新增商品后发送过来的id消息，查询数据库得到此tbItem对应的dto对象
    SearchItemDTO getItemById(Long itemId);

}
