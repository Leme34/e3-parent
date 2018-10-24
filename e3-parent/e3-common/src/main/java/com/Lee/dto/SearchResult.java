package com.Lee.dto;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {

	private long recordCount;
	private int totalPages;
	private List<SearchItemDTO> itemList;
	public long getRecordCount() {
		return recordCount;
	}
	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
	public List<SearchItemDTO> getItemList() {
		return itemList;
	}
	public void setItemList(List<SearchItemDTO> itemList) {
		this.itemList = itemList;
	}
	
}
