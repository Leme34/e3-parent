package com.Lee.service;

import com.Lee.dto.E3Result;
import com.Lee.dto.EasyUITreeNodeDTO;

import java.util.List;

public interface ContentCategoryService {

	List<EasyUITreeNodeDTO> getContentCatList(long parentId);
	E3Result addContentCategory(long parentId, String name);
}
