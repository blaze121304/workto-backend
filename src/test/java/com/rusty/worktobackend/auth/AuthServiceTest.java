package com.rusty.worktobackend.auth;

import com.rusty.worktobackend.common.exception.AppException;
import com.rusty.worktobackend.common.util.JwtTokenProvider;
import com.rusty.worktobackend.domain.dto.SignupRequest;
import com.rusty.worktobackend.domain.entity.User;
import com.rusty.worktobackend.repository.dao.UserRepository;
import com.rusty.worktobackend.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void signup_success() {
        SignupRequest request = buildRequest("test@example.com", "password123", "홍길동");
        given(userRepository.existsByEmail("test@example.com")).willReturn(false);
        given(passwordEncoder.encode("password123")).willReturn("encoded");
        given(userRepository.save(any(User.class))).willAnswer(inv -> inv.getArgument(0));

        authService.signup(request);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void signup_duplicateEmail_throws409() {
        SignupRequest request = buildRequest("dupe@example.com", "password123", "중복유저");
        given(userRepository.existsByEmail("dupe@example.com")).willReturn(true);

        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(AppException.class)
                .satisfies(ex -> assertThat(((AppException) ex).getStatus()).isEqualTo(HttpStatus.CONFLICT));
    }

    private SignupRequest buildRequest(String email, String password, String nickname) {
        try {
            SignupRequest req = new SignupRequest();
            setField(req, "email", email);
            setField(req, "password", password);
            setField(req, "nickname", nickname);
            return req;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
