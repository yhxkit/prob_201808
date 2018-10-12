package com.home.test180924.util;

import org.springframework.data.domain.PageRequest;

public interface Paging {
    PageRequest elementsByPage(int requiredPage, int elementsNumberForOnePage, String sortBy);
}
