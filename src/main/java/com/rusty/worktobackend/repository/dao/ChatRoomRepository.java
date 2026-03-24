package com.rusty.worktobackend.repository.dao;

import com.rusty.worktobackend.domain.entity.ChatRoom;
import com.rusty.worktobackend.domain.entity.MarketPost;
import com.rusty.worktobackend.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByMarketPostAndBuyer(MarketPost marketPost, User buyer);

    @Query("SELECT r FROM ChatRoom r WHERE r.buyer = :user OR r.seller = :user ORDER BY r.createdAt DESC")
    List<ChatRoom> findAllByUser(@Param("user") User user);

    List<ChatRoom> findAllByMarketPost(MarketPost marketPost);
}
