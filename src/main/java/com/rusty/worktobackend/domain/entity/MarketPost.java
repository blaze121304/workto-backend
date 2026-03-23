package com.rusty.worktobackend.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MarketPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MarketStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(nullable = false)
    private int favorite;

    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = MarketStatus.SALE;
        this.favorite = 0;
    }

    public static MarketPost of(String title, int price, String description, String imageUrl, User author) {
        MarketPost post = new MarketPost();
        post.title = title;
        post.price = price;
        post.description = description;
        post.imageUrl = imageUrl;
        post.author = author;
        return post;
    }

    public void markAsSold() {
        this.status = MarketStatus.SOLD;
    }

    public void update(String title, int price, String description, String imageUrl) {
        this.title = title;
        this.price = price;
        this.description = description;
        if (imageUrl != null) {
            this.imageUrl = imageUrl;
        }
    }

    public void addFavorite() {
        this.favorite++;
    }

    public void removeFavorite() {
        if (this.favorite > 0) this.favorite--;
    }
}
