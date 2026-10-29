package org.sopt.dto;

public record PostResponse(
        Long id,
        String title,
        String content,
        String authorName,
        int viewCount
) {
}