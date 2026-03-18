package com.rusty.worktobackend.domain.dto;

import com.rusty.worktobackend.domain.entity.Post;

import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String content,
        String authorNickname,
        LocalDateTime createdAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getContent(),
                post.getAuthor().getNickname(),
                post.getCreatedAt()
        );
    }
}
