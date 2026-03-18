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
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<PostResponse> listPosts() {
        return postService.listPosts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PostResponse createPost(@Valid @RequestBody PostRequest request,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        return postService.createPost(request, userDetails.getUsername());
    }
}
