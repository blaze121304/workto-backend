package com.rusty.worktobackend.domain.dto;

import com.rusty.worktobackend.domain.entity.WalkRoom;

import java.time.LocalDateTime;
import java.util.List;

public record RoomResponse(
        Long id,
        String title,
        String meetPlace,
        int maxParticipants,
        long currentCount,
        boolean full,
        boolean joined,
        LocalDateTime meetTime,
        String creatorNickname,
        List<String> participantNicknames
) {
    public static RoomResponse of(WalkRoom room, List<String> participantNicknames, boolean joined) {
        return new RoomResponse(
                room.getId(),
                room.getTitle(),
                room.getMeetPlace(),
                room.getMaxParticipants(),
                participantNicknames.size(),
                participantNicknames.size() >= room.getMaxParticipants(),
                joined,
                room.getMeetTime(),
                room.getCreator().getNickname(),
                participantNicknames
        );
    }
}
