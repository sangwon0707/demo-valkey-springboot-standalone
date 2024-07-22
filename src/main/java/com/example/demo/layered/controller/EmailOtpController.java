//package com.example.demo.layered.controller;
//
//import com.example.demo.layered.entity.EmailOtp;
//import com.example.demo.layered.service.EmailOtpService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/otp")
//@RequiredArgsConstructor
//public class EmailOtpController {
//
//    private final EmailOtpService service;
//
//    @GetMapping("/{id}")
//    public EmailOtp getOtp(@PathVariable String id) {
//        return service.read(id);
//    }
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public EmailOtp createOtp(@RequestBody EmailOtp emailOtp) {
//        return service.create(emailOtp);
//    }
//
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public EmailOtp deleteOtp(@PathVariable String id) {
//        return service.delete(id);
//    }
//
//    @PostMapping("/refresh")
//    public EmailOtp refreshToken(@RequestParam String refreshToken) {
//        return service.refreshToken(refreshToken);
//    }
//}

package com.example.demo.layered.controller;

import com.example.demo.layered.controller.dto.EmailOtpDto;
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
    public EmailOtpDto.Response getOtp(@PathVariable String id) {
        return service.read(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmailOtpDto.Response createOtp(@RequestBody EmailOtpDto.CreateRequest request) {
        return service.create(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public EmailOtpDto.Response deleteOtp(@PathVariable String id) {
        return service.delete(id);
    }

    @PostMapping("/refresh")
    public EmailOtpDto.Response refreshToken(@RequestBody EmailOtpDto.RefreshRequest request) {
        return service.refreshToken(request.refreshToken());
    }
}

