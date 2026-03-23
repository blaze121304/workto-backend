package com.rusty.worktobackend.controller;

import com.rusty.worktobackend.domain.dto.AnonPostResponse;
import com.rusty.worktobackend.domain.dto.PostRequest;
import com.rusty.worktobackend.service.AnonPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/anon-posts")
@RequiredArgsConstructor
public class AnonPostController {

    private final AnonPostService anonPostService;

    @GetMapping
    public List<AnonPostResponse> listPosts() {
        return anonPostService.listPosts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AnonPostResponse createPost(@Valid @RequestBody PostRequest request,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        return anonPostService.createPost(request, userDetails.getUsername());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetails userDetails) {
        anonPostService.deletePost(id, userDetails.getUsername());
    }
}
