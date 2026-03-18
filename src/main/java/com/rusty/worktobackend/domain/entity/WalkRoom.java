package com.rusty.worktobackend.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalkRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String meetPlace;

    @Column(nullable = false)
    private int maxParticipants;

    private LocalDateTime meetTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public static WalkRoom of(String title, String meetPlace, int maxParticipants, LocalDateTime meetTime, User creator) {
        WalkRoom room = new WalkRoom();
        room.title = title;
        room.meetPlace = meetPlace;
        room.maxParticipants = maxParticipants;
        room.meetTime = meetTime;
        room.creator = creator;
        return room;
    }
}
