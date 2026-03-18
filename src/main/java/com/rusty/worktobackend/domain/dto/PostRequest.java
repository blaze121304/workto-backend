package com.rusty.worktobackend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostRequest {

    @NotBlank
    private String content;
}
