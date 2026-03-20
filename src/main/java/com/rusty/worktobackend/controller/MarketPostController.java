package com.rusty.worktobackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rusty.worktobackend.domain.dto.MarketPostRequest;
import com.rusty.worktobackend.domain.dto.MarketPostResponse;
import com.rusty.worktobackend.service.MarketPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
public class MarketPostController {

    private final MarketPostService marketPostService;
    private final ObjectMapper objectMapper;

    @GetMapping
    public List<MarketPostResponse> listPosts() {
        return marketPostService.listPosts();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public MarketPostResponse createPost(@RequestPart(value = "image", required = false) MultipartFile image,
                                         @RequestPart("data") String data,
                                         @AuthenticationPrincipal UserDetails userDetails) throws IOException {
        MarketPostRequest request = objectMapper.readValue(data, MarketPostRequest.class);
        return marketPostService.createPost(image, request, userDetails.getUsername());
    }

    @PatchMapping("/{id}/sold")
    public MarketPostResponse markAsSold(@PathVariable Long id,
                                         @AuthenticationPrincipal UserDetails userDetails) {
        return marketPostService.markAsSold(id, userDetails.getUsername());
    }
}
