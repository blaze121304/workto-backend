package com.rusty.worktobackend.repository.dao;

import com.rusty.worktobackend.domain.entity.User;
import com.rusty.worktobackend.domain.entity.WalkJoin;
import com.rusty.worktobackend.domain.entity.WalkRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalkJoinRepository extends JpaRepository<WalkJoin, Long> {
    boolean existsByRoomAndUser(WalkRoom room, User user);
    long countByRoom(WalkRoom room);
    Optional<WalkJoin> findByRoomAndUser(WalkRoom room, User user);
    List<WalkJoin> findAllByRoom(WalkRoom room);
    void deleteAllByRoom(WalkRoom room);
    void deleteByRoomAndUser(WalkRoom room, User user);
}
