package com.home.test180924.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PagingImpl implements Paging{
    public PageRequest elementsByPage(int requiredPage, int elementsNumberForOnePage, String sortBy){
        Sort sort = Sort.by( new Sort.Order(Sort.Direction.DESC, sortBy)) ;
        PageRequest pageRequest = PageRequest.of(requiredPage, elementsNumberForOnePage, sort);
        return  pageRequest;
    }
}
