package com.example.demo.layered.usecase;

import com.example.demo.layered.controller.dto.EmailOtpDto;

public interface EmailOtpRefreshUseCase {
    EmailOtpDto.Response refreshToken(String refreshToken);
}