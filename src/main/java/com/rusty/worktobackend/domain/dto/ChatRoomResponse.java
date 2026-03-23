package com.rusty.worktobackend.domain.dto;

import com.rusty.worktobackend.domain.entity.ChatRoom;

import java.time.LocalDateTime;

public record ChatRoomResponse(
        Long id,
        Long marketPostId,
        String marketPostTitle,
        String buyerNickname,
        String sellerNickname,
        LocalDateTime createdAt
) {
    public static ChatRoomResponse from(ChatRoom room) {
        return new ChatRoomResponse(
                room.getId(),
                room.getMarketPost().getId(),
                room.getMarketPost().getTitle(),
                room.getBuyer().getNickname(),
                room.getSeller().getNickname(),
                room.getCreatedAt()
        );
    }
}
