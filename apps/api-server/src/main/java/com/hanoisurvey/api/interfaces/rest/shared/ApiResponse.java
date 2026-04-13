package com.hanoisurvey.api.interfaces.rest.shared;

import java.util.Map;

public record ApiResponse<T>(boolean success, T data, Map<String, Object> meta, String traceId) {

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null, null);
    }

    public static <T> ApiResponse<T> ok(T data, Map<String, Object> meta) {
        return new ApiResponse<>(true, data, meta, null);
    }
}
