package com.Lee.service;

import com.Lee.dto.EasyUITreeNodeDTO;

import java.util.List;

public interface ItemCatService {

	List<EasyUITreeNodeDTO> getItemCatlist(long parentId);
}
