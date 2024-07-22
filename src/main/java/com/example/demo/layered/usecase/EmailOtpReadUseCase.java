//package com.example.demo.layered.usecase;
//
//import com.example.demo.layered.entity.EmailOtp;
//
//public interface EmailOtpReadUseCase {
////    EmailOtp read(String id);
//    EmailOtp read(String email);
//}

package com.example.demo.layered.usecase;

import com.example.demo.layered.controller.dto.EmailOtpDto;

public interface EmailOtpReadUseCase {
    EmailOtpDto.Response read(String idOrEmail);
}