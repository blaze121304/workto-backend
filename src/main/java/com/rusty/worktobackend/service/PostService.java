package com.rusty.worktobackend.service;

import com.rusty.worktobackend.common.exception.AppException;
import com.rusty.worktobackend.domain.dto.PostRequest;
import com.rusty.worktobackend.domain.dto.PostResponse;
import com.rusty.worktobackend.domain.entity.Post;
import com.rusty.worktobackend.domain.entity.User;
import com.rusty.worktobackend.domain.entity.WalkRoom;
import com.rusty.worktobackend.repository.dao.PostRepository;
import com.rusty.worktobackend.repository.dao.UserRepository;
import com.rusty.worktobackend.repository.dao.WalkJoinRepository;
import com.rusty.worktobackend.repository.dao.WalkRoomRepository;
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
    private final WalkRoomRepository walkRoomRepository;
    private final WalkJoinRepository walkJoinRepository;

    @Transactional(readOnly = true)
    public List<PostResponse> listPosts(Long roomId) {
        WalkRoom room = getRoom(roomId);
        return postRepository.findAllByRoomOrderByCreatedAtDesc(room).stream()
                .map(PostResponse::from)
                .toList();
    }

    @Transactional
    public PostResponse createPost(Long roomId, PostRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));
        WalkRoom room = getRoom(roomId);

        if (!walkJoinRepository.existsByRoomAndUser(room, user)) {
            throw new AppException(HttpStatus.FORBIDDEN, "방 참여자만 글을 작성할 수 있습니다.");
        }

        Post post = postRepository.save(Post.of(room, user, request.getContent()));
        return PostResponse.from(post);
    }

    @Transactional
    public void deletePost(Long roomId, Long postId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."));

        if (!post.getRoom().getId().equals(roomId)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "해당 방의 게시글이 아닙니다.");
        }
        if (!post.getAuthor().getId().equals(user.getId())) {
            throw new AppException(HttpStatus.FORBIDDEN, "본인이 작성한 글만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }

    private WalkRoom getRoom(Long roomId) {
        return walkRoomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "방을 찾을 수 없습니다."));
    }
}
