package com.rusty.worktobackend.service;

import com.rusty.worktobackend.common.exception.AppException;
import com.rusty.worktobackend.domain.dto.PostRequest;
import com.rusty.worktobackend.domain.dto.PostResponse;
import com.rusty.worktobackend.domain.entity.Post;
import com.rusty.worktobackend.domain.entity.User;
import com.rusty.worktobackend.repository.dao.PostRepository;
import com.rusty.worktobackend.repository.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<PostResponse> listPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(PostResponse::from)
                .toList();
    }

    @Transactional
    public PostResponse createPost(PostRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));
        Post post = postRepository.save(Post.of(user, request.getContent()));
        return PostResponse.from(post);
    }
}
