package com.rusty.worktobackend.repository.dao;

import com.rusty.worktobackend.domain.entity.AnonPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnonPostRepository extends JpaRepository<AnonPost, Long> {
    List<AnonPost> findAllByOrderByCreatedAtDesc();
}
