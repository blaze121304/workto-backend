package com.rusty.worktobackend.domain.dto;

import com.rusty.worktobackend.domain.entity.ChatMessage;

import java.time.LocalDateTime;

public record ChatMessageResponse(
        Long id,
        String senderNickname,
        String content,
        LocalDateTime createdAt
) {
    public static ChatMessageResponse from(ChatMessage message) {
        return new ChatMessageResponse(
                message.getId(),
                message.getSender().getNickname(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}
