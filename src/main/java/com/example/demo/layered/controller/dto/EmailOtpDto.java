package com.example.demo.layered.controller.dto;

import lombok.Builder;

public final class EmailOtpDto {
    private EmailOtpDto() {}

    @Builder
    public record CreateRequest(
            String email,
            String otp,
            Integer ttl
    ) {}

    @Builder
    public record Response(
            String id,
            String email,
            String otp,
            Integer ttl,
            String refreshToken
    ) {}

    @Builder
    public record RefreshRequest(
            String refreshToken
    ) {}
}