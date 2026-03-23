package com.rusty.worktobackend.domain.dto;

import com.rusty.worktobackend.domain.entity.AnonPost;

import java.time.LocalDateTime;

public record AnonPostResponse(
        Long id,
        String content,
        String authorNickname,
        LocalDateTime createdAt
) {
    public static AnonPostResponse from(AnonPost post) {
        return new AnonPostResponse(
                post.getId(),
                post.getContent(),
                maskNickname(post.getAuthor().getNickname()),
                post.getCreatedAt()
        );
    }

    private static String maskNickname(String nickname) {
        if (nickname == null) return "";
        String reversed = new StringBuilder(nickname).reverse().toString();
        return java.util.Base64.getEncoder().encodeToString(reversed.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }
}
