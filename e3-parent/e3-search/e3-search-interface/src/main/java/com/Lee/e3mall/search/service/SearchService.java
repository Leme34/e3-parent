package com.Lee.e3mall.search.service;

import com.Lee.dto.SearchResult;

public interface SearchService {

    public SearchResult search(String keyword, int page, int rows) throws Exception;
}
