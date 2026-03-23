package com.rusty.worktobackend.repository.dao;

import com.rusty.worktobackend.domain.entity.MarketFavorite;
import com.rusty.worktobackend.domain.entity.MarketPost;
import com.rusty.worktobackend.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MarketFavoriteRepository extends JpaRepository<MarketFavorite, Long> {
    Optional<MarketFavorite> findByUserAndMarketPost(User user, MarketPost marketPost);
    boolean existsByUserAndMarketPost(User user, MarketPost marketPost);
}
