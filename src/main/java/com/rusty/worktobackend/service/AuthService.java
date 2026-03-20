package com.rusty.worktobackend.service;

import com.rusty.worktobackend.common.exception.AppException;
import com.rusty.worktobackend.common.util.JwtTokenProvider;
import com.rusty.worktobackend.domain.dto.LoginRequest;
import com.rusty.worktobackend.domain.dto.MeResponse;
import com.rusty.worktobackend.domain.dto.SignupRequest;
import com.rusty.worktobackend.domain.dto.TokenResponse;
import com.rusty.worktobackend.domain.entity.User;
import com.rusty.worktobackend.repository.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다.");
        }
        userRepository.save(User.of(
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getNickname(),
                request.getDepartment()
        ));
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return new TokenResponse(jwtTokenProvider.generate(user.getEmail()));
    }

    @Transactional(readOnly = true)
    public MeResponse me(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다."));
        return new MeResponse(user.getEmail(), user.getNickname(), user.getDepartment());
    }
}
