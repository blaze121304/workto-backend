package com.rusty.worktobackend.controller;

import com.rusty.worktobackend.domain.dto.ChatMessageRequest;
import com.rusty.worktobackend.domain.dto.ChatMessageResponse;
import com.rusty.worktobackend.domain.dto.ChatRoomResponse;
import com.rusty.worktobackend.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatRoomResponse createRoom(@RequestParam Long marketPostId,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        return chatService.createRoom(marketPostId, userDetails.getUsername());
    }

    @GetMapping("/rooms")
    public List<ChatRoomResponse> listRooms(@AuthenticationPrincipal UserDetails userDetails) {
        return chatService.listRooms(userDetails.getUsername());
    }

    @GetMapping("/rooms/{roomId}/messages")
    public List<ChatMessageResponse> listMessages(@PathVariable Long roomId,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        return chatService.listMessages(roomId, userDetails.getUsername());
    }

    @PostMapping("/rooms/{roomId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatMessageResponse sendMessage(@PathVariable Long roomId,
                                           @Valid @RequestBody ChatMessageRequest request,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        return chatService.sendMessage(roomId, request, userDetails.getUsername());
    }

    @DeleteMapping("/rooms/{roomId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leaveRoom(@PathVariable Long roomId,
                          @AuthenticationPrincipal UserDetails userDetails) {
        chatService.leaveRoom(roomId, userDetails.getUsername());
    }
}
