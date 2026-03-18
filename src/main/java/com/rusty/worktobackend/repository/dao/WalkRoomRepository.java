package com.rusty.worktobackend.repository.dao;

import com.rusty.worktobackend.domain.entity.User;
import com.rusty.worktobackend.domain.entity.WalkRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalkRoomRepository extends JpaRepository<WalkRoom, Long> {
    boolean existsByCreator(User creator);
}
