package org.sopt.common;

public record ApiResponse<T>(
        String code,
        String message,
        T data
) {
    public static <T> ApiResponse<T> success(
            String code,
            String message,
            T data
    ) {
        return new ApiResponse<>(code, message, data);
    }

    public static ApiResponse<Void> failure(
            String code,
            String message
    ) {
        return new ApiResponse<>(code, message, null);
    }
}