package com.rusty.worktobackend.repository.dao;

import com.rusty.worktobackend.domain.entity.ChatMessage;
import com.rusty.worktobackend.domain.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByRoomOrderByCreatedAtAsc(ChatRoom room);
    void deleteAllByRoom(ChatRoom room);
}
