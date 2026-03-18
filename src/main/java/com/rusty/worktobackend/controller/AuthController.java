package com.rusty.worktobackend.controller;

import com.rusty.worktobackend.domain.dto.LoginRequest;
import com.rusty.worktobackend.domain.dto.MeResponse;
import com.rusty.worktobackend.domain.dto.SignupRequest;
import com.rusty.worktobackend.domain.dto.TokenResponse;
import com.rusty.worktobackend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public MeResponse me(@AuthenticationPrincipal UserDetails userDetails) {
        return authService.me(userDetails.getUsername());
    }
}
