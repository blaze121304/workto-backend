package com.rusty.worktobackend.service;

import com.rusty.worktobackend.common.exception.AppException;
import com.rusty.worktobackend.domain.dto.MarketPostRequest;
import com.rusty.worktobackend.domain.dto.MarketPostResponse;
import com.rusty.worktobackend.domain.entity.MarketFavorite;
import com.rusty.worktobackend.domain.entity.MarketPost;
import com.rusty.worktobackend.domain.entity.User;
import com.rusty.worktobackend.repository.dao.MarketFavoriteRepository;
import com.rusty.worktobackend.repository.dao.MarketPostRepository;
import com.rusty.worktobackend.repository.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MarketPostService {

    private final MarketPostRepository marketPostRepository;
    private final MarketFavoriteRepository marketFavoriteRepository;
    private final UserRepository userRepository;

    @Value("${app.image.upload-dir}")
    private String uploadDir;

    @Transactional(readOnly = true)
    public List<MarketPostResponse> listPosts() {
        return marketPostRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(MarketPostResponse::from)
                .toList();
    }

    @Transactional
    public MarketPostResponse createPost(MultipartFile image, MarketPostRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));

        String imageUrl = (image != null && !image.isEmpty()) ? saveImage(image) : null;
        MarketPost post = marketPostRepository.save(MarketPost.of(request.getTitle(), request.getPrice(), request.getDescription(), imageUrl, user));
        return MarketPostResponse.from(post);
    }

    @Transactional
    public MarketPostResponse markAsSold(Long postId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));
        MarketPost post = marketPostRepository.findById(postId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new AppException(HttpStatus.FORBIDDEN, "본인 게시글만 상태를 변경할 수 있습니다.");
        }

        post.markAsSold();
        return MarketPostResponse.from(post);
    }

    @Transactional
    public MarketPostResponse updatePost(Long postId, MultipartFile image, MarketPostRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));
        MarketPost post = marketPostRepository.findById(postId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new AppException(HttpStatus.FORBIDDEN, "본인 게시글만 수정할 수 있습니다.");
        }

        String imageUrl = (image != null && !image.isEmpty()) ? saveImage(image) : null;
        post.update(request.getTitle(), request.getPrice(), request.getDescription(), imageUrl);
        return MarketPostResponse.from(post);
    }

    @Transactional
    public void deletePost(Long postId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));
        MarketPost post = marketPostRepository.findById(postId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new AppException(HttpStatus.FORBIDDEN, "본인 게시글만 삭제할 수 있습니다.");
        }

        deleteImageFile(post.getImageUrl());
        marketPostRepository.delete(post);
    }

    @Transactional
    public MarketPostResponse toggleFavorite(Long postId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));
        MarketPost post = marketPostRepository.findById(postId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        marketFavoriteRepository.findByUserAndMarketPost(user, post)
                .ifPresentOrElse(
                        existing -> {
                            marketFavoriteRepository.delete(existing);
                            post.removeFavorite();
                        },
                        () -> {
                            marketFavoriteRepository.save(MarketFavorite.of(user, post));
                            post.addFavorite();
                        }
                );

        return MarketPostResponse.from(post);
    }

    private void deleteImageFile(String imageUrl) {
        if (imageUrl == null) return;
        try {
            String filename = imageUrl.replace("/images/", "");
            Path filePath = Paths.get(uploadDir).resolve(filename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // 파일 삭제 실패는 무시 (게시글 삭제는 진행)
        }
    }

    private String saveImage(MultipartFile image) {
        try {
            String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Files.copy(image.getInputStream(), uploadPath.resolve(filename));
            return "/images/" + filename;
        } catch (IOException e) {
            throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 저장에 실패했습니다.");
        }
    }
}
