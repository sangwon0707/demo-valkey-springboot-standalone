package com.example.demo.layered.mapper;

import com.example.demo.layered.entity.EmailOtp;
import org.springframework.stereotype.Component;

@Component
public class EmailOtpMapper {

    public EmailOtp copy(EmailOtp source) {
        if (source == null) {
            return null;
        }

        EmailOtp target = new EmailOtp();
        target.setId(source.getId());
        target.setEmail(source.getEmail());
        target.setOtp(source.getOtp());
        target.setTtl(source.getTtl());
        return target;
    }

    public void update(EmailOtp target, EmailOtp source) {
        if (source == null || target == null) {
            return;
        }

        target.setEmail(source.getEmail());
        target.setOtp(source.getOtp());
        target.setTtl(source.getTtl());
    }
}