//package com.example.demo.layered.usecase;
//
//import com.example.demo.layered.entity.EmailOtp;
//
//public interface EmailOtpCreateUseCase {
//    EmailOtp create(EmailOtp emailOtp);
//}

package com.example.demo.layered.usecase;

import com.example.demo.layered.controller.dto.EmailOtpDto;

public interface EmailOtpCreateUseCase {
    EmailOtpDto.Response create(EmailOtpDto.CreateRequest request);
}