package com.rusty.worktobackend.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "market_post_id"}))
public class MarketFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "market_post_id", nullable = false)
    private MarketPost marketPost;

    public static MarketFavorite of(User user, MarketPost marketPost) {
        MarketFavorite favorite = new MarketFavorite();
        favorite.user = user;
        favorite.marketPost = marketPost;
        return favorite;
    }
}
