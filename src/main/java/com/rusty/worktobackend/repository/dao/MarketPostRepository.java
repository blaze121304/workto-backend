package com.rusty.worktobackend.repository.dao;

import com.rusty.worktobackend.domain.entity.MarketPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketPostRepository extends JpaRepository<MarketPost, Long> {
    List<MarketPost> findAllByOrderByCreatedAtDesc();
}
