package com.rusty.worktobackend.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MarketPostRequest {

    @NotBlank
    private String title;

    @Min(0)
    private int price;

    @NotBlank
    private String description;
}
