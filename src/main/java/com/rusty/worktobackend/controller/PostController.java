package com.rusty.worktobackend.controller;

import com.rusty.worktobackend.domain.dto.PostRequest;
import com.rusty.worktobackend.domain.dto.PostResponse;
import com.rusty.worktobackend.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms/{roomId}/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<PostResponse> listPosts(@PathVariable Long roomId) {
        return postService.listPosts(roomId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse createPost(@PathVariable Long roomId,
                                   @Valid @RequestBody PostRequest request,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        return postService.createPost(roomId, request, userDetails.getUsername());
    }

    @DeleteMapping("/{postId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long roomId,
                           @PathVariable Long postId,
                           @AuthenticationPrincipal UserDetails userDetails) {
        postService.deletePost(roomId, postId, userDetails.getUsername());
    }
}
