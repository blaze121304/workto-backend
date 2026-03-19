package com.rusty.worktobackend.repository.dao;

import com.rusty.worktobackend.domain.entity.Post;
import com.rusty.worktobackend.domain.entity.WalkRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByRoomOrderByCreatedAtDesc(WalkRoom room);
    void deleteAllByRoom(WalkRoom room);
}
