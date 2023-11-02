package com.gdsc.moa.global.paging;

import org.springframework.data.domain.Page;

public class PagingUtil {

    public static <T> PageResponse<T> toResponse(Page<T> page) {
        Long nextPage = page.isLast() ? null : (long) page.getNumber() + 2;
        return new PageResponse<>(
                page.getContent(),
                page.getTotalElements(),
                nextPage
        );
    }

}
