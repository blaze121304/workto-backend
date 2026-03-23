package com.rusty.worktobackend.service;

import com.rusty.worktobackend.common.exception.AppException;
import com.rusty.worktobackend.domain.dto.AnonPostResponse;
import com.rusty.worktobackend.domain.dto.PostRequest;
import com.rusty.worktobackend.domain.entity.AnonPost;
import com.rusty.worktobackend.domain.entity.User;
import com.rusty.worktobackend.repository.dao.AnonPostRepository;
import com.rusty.worktobackend.repository.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnonPostService {

    private final AnonPostRepository anonPostRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<AnonPostResponse> listPosts() {
        return anonPostRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(AnonPostResponse::from)
                .toList();
    }

    @Transactional
    public AnonPostResponse createPost(PostRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));
        AnonPost post = anonPostRepository.save(AnonPost.of(user, request.getContent()));
        return AnonPostResponse.from(post);
    }

    @Transactional
    public void deletePost(Long postId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));
        AnonPost post = anonPostRepository.findById(postId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new AppException(HttpStatus.FORBIDDEN, "본인이 작성한 글만 삭제할 수 있습니다.");
        }

        anonPostRepository.delete(post);
    }
}
