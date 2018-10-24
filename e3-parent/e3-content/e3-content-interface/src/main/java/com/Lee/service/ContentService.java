package com.Lee.service;

import com.Lee.dto.E3Result;
import com.Lee.dto.EasyUIDataGridResultDTO;
import com.Lee.pojo.TbContent;

import java.util.List;

public interface ContentService {

	E3Result addContent(TbContent content);
	List<TbContent> getContentListByCid(long cid);
	EasyUIDataGridResultDTO getContentListPageByCid(long cid, int page, int rows);
	void deleteById(long id);
}
