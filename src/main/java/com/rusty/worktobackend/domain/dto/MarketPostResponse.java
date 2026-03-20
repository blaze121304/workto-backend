package com.rusty.worktobackend.domain.dto;

import com.rusty.worktobackend.domain.entity.MarketPost;

import java.time.LocalDateTime;

public record MarketPostResponse(
        Long id,
        String title,
        int price,
        String description,
        String imageUrl,
        String status,
        String authorNickname,
        String authorDepartment,
        LocalDateTime createdAt
) {
    public static MarketPostResponse from(MarketPost post) {
        return new MarketPostResponse(
                post.getId(),
                post.getTitle(),
                post.getPrice(),
                post.getDescription(),
                post.getImageUrl(),
                post.getStatus().name(),
                post.getAuthor().getNickname(),
                post.getAuthor().getDepartment(),
                post.getCreatedAt()
        );
    }
}
