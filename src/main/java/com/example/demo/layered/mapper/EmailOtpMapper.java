//package com.example.demo.layered.mapper;
//
//import com.example.demo.layered.entity.EmailOtp;
//import org.springframework.stereotype.Component;
//
//@Component
//public class EmailOtpMapper {
//
//    public EmailOtp copy(EmailOtp source) {
//        if (source == null) {
//            return null;
//        }
//
//        EmailOtp target = new EmailOtp();
//        target.setId(source.getId());
//        target.setEmail(source.getEmail());
//        target.setOtp(source.getOtp());
//        target.setTtl(source.getTtl());
//        return target;
//    }
//
//    public void update(EmailOtp target, EmailOtp source) {
//        if (source == null || target == null) {
//            return;
//        }
//
//        target.setEmail(source.getEmail());
//        target.setOtp(source.getOtp());
//        target.setTtl(source.getTtl());
//    }
//}

package com.example.demo.layered.mapper;

import com.example.demo.layered.controller.dto.EmailOtpDto;
import com.example.demo.layered.entity.EmailOtp;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmailOtpMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    EmailOtp toEntity(EmailOtpDto.CreateRequest dto);

    EmailOtpDto.Response toDto(EmailOtp entity);

    EmailOtp copy(EmailOtp source);
}