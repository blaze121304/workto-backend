package com.rusty.worktobackend.service;

import com.rusty.worktobackend.common.exception.AppException;
import com.rusty.worktobackend.domain.dto.RoomRequest;
import com.rusty.worktobackend.domain.dto.RoomResponse;
import com.rusty.worktobackend.domain.entity.User;
import com.rusty.worktobackend.domain.entity.WalkJoin;
import com.rusty.worktobackend.domain.entity.WalkRoom;
import com.rusty.worktobackend.repository.dao.UserRepository;
import com.rusty.worktobackend.repository.dao.WalkJoinRepository;
import com.rusty.worktobackend.repository.dao.WalkRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final WalkRoomRepository walkRoomRepository;
    private final WalkJoinRepository walkJoinRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<RoomResponse> listRooms(String email) {
        User user = getUser(email);
        return walkRoomRepository.findAll().stream()
                .map(room -> {
                    List<String> nicknames = getNicknames(room);
                    boolean joined = walkJoinRepository.existsByRoomAndUser(room, user);
                    return RoomResponse.of(room, nicknames, joined);
                })
                .toList();
    }

    @Transactional
    public RoomResponse createRoom(RoomRequest request, String email) {
        User user = getUser(email);
        if (walkRoomRepository.existsByCreator(user)) {
            throw new AppException(HttpStatus.CONFLICT, "이미 생성한 방이 있습니다.");
        }
        WalkRoom room = walkRoomRepository.save(
                WalkRoom.of(request.getTitle(), request.getMeetPlace(), request.getMaxParticipants(), request.getMeetTime(), user)
        );
        walkJoinRepository.save(WalkJoin.of(room, user));
        return RoomResponse.of(room, List.of(user.getNickname()), true);
    }

    @Transactional
    public void joinRoom(Long roomId, String email) {
        User user = getUser(email);
        WalkRoom room = getRoom(roomId);

        if (walkJoinRepository.existsByRoomAndUser(room, user)) {
            throw new AppException(HttpStatus.BAD_REQUEST, "이미 참여한 방입니다.");
        }
        if (walkJoinRepository.countByRoom(room) >= room.getMaxParticipants()) {
            throw new AppException(HttpStatus.CONFLICT, "마감된 방입니다.");
        }

        walkJoinRepository.save(WalkJoin.of(room, user));
    }

    @Transactional
    public void leaveRoom(Long roomId, String email) {
        User user = getUser(email);
        WalkRoom room = getRoom(roomId);

        walkJoinRepository.findByRoomAndUser(room, user)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "참여하지 않은 방입니다."));

        if (room.getCreator().getId().equals(user.getId())) {
            walkJoinRepository.deleteAllByRoom(room);
            walkRoomRepository.delete(room);
        } else {
            walkJoinRepository.deleteByRoomAndUser(room, user);
        }
    }

    private List<String> getNicknames(WalkRoom room) {
        return walkJoinRepository.findAllByRoom(room).stream()
                .map(join -> join.getUser().getNickname())
                .toList();
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));
    }

    private WalkRoom getRoom(Long roomId) {
        return walkRoomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "방을 찾을 수 없습니다."));
    }
}
