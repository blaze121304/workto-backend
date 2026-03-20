package com.rusty.worktobackend.common.config;

import com.rusty.worktobackend.domain.entity.User;
import com.rusty.worktobackend.repository.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        for (int i = 1; i <= 10; i++) {
            String email = i + "@test.com";
            String password = String.valueOf(i);
            if (!userRepository.existsByEmail(email)) {
                String department = (i % 2 == 1) ? "10층 개발팀" : "9층 기획팀";
                userRepository.save(User.of(email, passwordEncoder.encode(password), "유저" + i, department));
            }
        }
    }
}
