package com.rusty.worktobackend.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RoomRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String meetPlace;

    @Min(2) @Max(10)
    private int maxParticipants;

    @NotNull
    private LocalDateTime meetTime;
}
