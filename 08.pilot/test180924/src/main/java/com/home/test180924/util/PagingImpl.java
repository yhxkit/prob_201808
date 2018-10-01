package com.home.test180924.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PagingImpl implements Paging{

    //private int elementsNumberForOnePage = 3; // 한페이지당 개씩만 출력한다고 하자..

    public PageRequest elementsByPage(int requiredPage, int elementsNumberForOnePage, String sortBy){
        Sort sort = Sort.by( new Sort.Order(Sort.Direction.DESC, sortBy)) ;
        PageRequest pageRequest = PageRequest.of(requiredPage, elementsNumberForOnePage, sort);
        return  pageRequest;
    }
}
