package com.rusty.worktobackend.service;

import com.rusty.worktobackend.common.exception.AppException;
import com.rusty.worktobackend.domain.dto.ChatMessageRequest;
import com.rusty.worktobackend.domain.dto.ChatMessageResponse;
import com.rusty.worktobackend.domain.dto.ChatRoomResponse;
import com.rusty.worktobackend.domain.entity.ChatMessage;
import com.rusty.worktobackend.domain.entity.ChatRoom;
import com.rusty.worktobackend.domain.entity.MarketPost;
import com.rusty.worktobackend.domain.entity.User;
import com.rusty.worktobackend.repository.dao.ChatMessageRepository;
import com.rusty.worktobackend.repository.dao.ChatRoomRepository;
import com.rusty.worktobackend.repository.dao.MarketPostRepository;
import com.rusty.worktobackend.repository.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MarketPostRepository marketPostRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatRoomResponse createRoom(Long marketPostId, String email) {
        User buyer = getUser(email);
        MarketPost post = marketPostRepository.findById(marketPostId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        if (post.getAuthor().getId().equals(buyer.getId())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "본인 게시글에는 채팅을 걸 수 없습니다.");
        }

        ChatRoom room = chatRoomRepository.findByMarketPostAndBuyer(post, buyer)
                .orElseGet(() -> chatRoomRepository.save(ChatRoom.of(post, buyer, post.getAuthor())));

        return ChatRoomResponse.from(room);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> listRooms(String email) {
        User user = getUser(email);
        return chatRoomRepository.findAllByUser(user).stream()
                .map(ChatRoomResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> listMessages(Long roomId, String email) {
        User user = getUser(email);
        ChatRoom room = getRoom(roomId);
        validateParticipant(room, user);
        return chatMessageRepository.findAllByRoomOrderByCreatedAtAsc(room).stream()
                .map(ChatMessageResponse::from)
                .toList();
    }

    @Transactional
    public ChatMessageResponse sendMessage(Long roomId, ChatMessageRequest request, String email) {
        User sender = getUser(email);
        ChatRoom room = getRoom(roomId);
        validateParticipant(room, sender);
        ChatMessage message = chatMessageRepository.save(ChatMessage.of(room, sender, request.getContent()));
        return ChatMessageResponse.from(message);
    }

    @Transactional
    public void leaveRoom(Long roomId, String email) {
        User user = getUser(email);
        ChatRoom room = getRoom(roomId);
        validateParticipant(room, user);
        chatMessageRepository.deleteAllByRoom(room);
        chatRoomRepository.delete(room);
    }

    private void validateParticipant(ChatRoom room, User user) {
        boolean isParticipant = room.getBuyer().getId().equals(user.getId())
                || room.getSeller().getId().equals(user.getId());
        if (!isParticipant) {
            throw new AppException(HttpStatus.FORBIDDEN, "채팅방 참여자가 아닙니다.");
        }
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));
    }

    private ChatRoom getRoom(Long roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "채팅방을 찾을 수 없습니다."));
    }
}
