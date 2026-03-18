package com.rusty.worktobackend.controller;

import com.rusty.worktobackend.domain.dto.RoomRequest;
import com.rusty.worktobackend.domain.dto.RoomResponse;
import com.rusty.worktobackend.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping
    public List<RoomResponse> listRooms(@AuthenticationPrincipal UserDetails userDetails) {
        return roomService.listRooms(userDetails.getUsername());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoomResponse createRoom(@Valid @RequestBody RoomRequest request,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        return roomService.createRoom(request, userDetails.getUsername());
    }

    @PostMapping("/{id}/join")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void joinRoom(@PathVariable Long id,
                         @AuthenticationPrincipal UserDetails userDetails) {
        roomService.joinRoom(id, userDetails.getUsername());
    }

    @DeleteMapping("/{id}/leave")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leaveRoom(@PathVariable Long id,
                          @AuthenticationPrincipal UserDetails userDetails) {
        roomService.leaveRoom(id, userDetails.getUsername());
    }
}
