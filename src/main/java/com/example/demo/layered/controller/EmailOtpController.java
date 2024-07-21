package com.example.demo.layered.controller;

import com.example.demo.layered.entity.EmailOtp;
import com.example.demo.layered.service.EmailOtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class EmailOtpController {

    private final EmailOtpService service;

    @GetMapping("/{id}")
    public EmailOtp getOtp(@PathVariable String id) {
        return service.read(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmailOtp createOtp(@RequestBody EmailOtp emailOtp) {
        return service.create(emailOtp);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOtp(@PathVariable String id) {
        service.delete(id);
    }

    @PostMapping("/refresh")
    public EmailOtp refreshToken(@RequestParam String refreshToken) {
        return service.refreshToken(refreshToken);
    }
}