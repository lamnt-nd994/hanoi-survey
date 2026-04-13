package com.hanoisurvey.api.interfaces.rest.shared;

import org.springframework.data.domain.Page;

import java.util.Map;

public final class PagingMeta {

    private PagingMeta() {
    }

    public static Map<String, Object> from(Page<?> page) {
        return Map.of(
                "page", page.getNumber() + 1,
                "size", page.getSize(),
                "total", page.getTotalElements(),
                "totalPages", page.getTotalPages()
        );
    }
}
