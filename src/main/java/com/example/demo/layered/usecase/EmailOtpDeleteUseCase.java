//package com.example.demo.layered.usecase;
//
//import com.example.demo.layered.entity.EmailOtp;
//
//public interface EmailOtpDeleteUseCase {
//    EmailOtp delete(String id);
//}

package com.example.demo.layered.usecase;

import com.example.demo.layered.controller.dto.EmailOtpDto;

public interface EmailOtpDeleteUseCase {
    EmailOtpDto.Response delete(String idOrEmail);
}