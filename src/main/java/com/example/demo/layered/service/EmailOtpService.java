//package com.example.demo.layered.service;
//
//import com.example.demo.layered.entity.EmailOtp;
//import com.example.demo.layered.mapper.EmailOtpMapper;
//import com.example.demo.layered.repository.EmailOtpRepository;
//import com.example.demo.layered.usecase.EmailOtpCreateUseCase;
//import com.example.demo.layered.usecase.EmailOtpDeleteUseCase;
//import com.example.demo.layered.usecase.EmailOtpReadUseCase;
//import com.example.demo.layered.util.SecureRandomUtil;
//import jakarta.validation.constraints.Email;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class EmailOtpService implements EmailOtpCreateUseCase, EmailOtpDeleteUseCase, EmailOtpReadUseCase {
//
//    private final EmailOtpRepository repository;
//    private final EmailOtpMapper mapper;
//
//    public EmailOtp create(EmailOtp emailOtp) {
//        String refreshToken = SecureRandomUtil.generateSecureToken(32);
//        emailOtp.setRefreshToken(refreshToken);
//        return repository.save(emailOtp);
//    }
//
//    @Override
//    public EmailOtp read(String id) {
////        return repository.findById(id).orElse(null);
//        EmailOtp emailOtp = repository.findById(id).orElse(null);
//        if (emailOtp == null) {
//            emailOtp = repository.findByEmail(id);
//        }
//        return emailOtp;
//    }
//
//    @Override
//    public EmailOtp delete(String id) {
//        EmailOtp emailOtp = repository.findById(id).orElse(null);
//        if (emailOtp != null) {
//            repository.deleteById(id);
//            return mapper.copy(emailOtp); // Using mapper to create a copy
//        }else {
//            emailOtp = repository.findByEmail(id);
//            if (emailOtp != null) {
//                repository.deleteByEmail(id);
//                System.out.println(mapper.copy(emailOtp));
//                return mapper.copy(emailOtp);
//            } else {
//                return null;
//            }
//        }
//    }
//
//
//
//    public EmailOtp refreshToken(String refreshToken) {
//        EmailOtp emailOtp = repository.findByRefreshToken(refreshToken);
//        if (emailOtp != null) {
//            String newRefreshToken = SecureRandomUtil.generateSecureToken(32);
//            emailOtp.setRefreshToken(newRefreshToken);
//            return repository.save(emailOtp);
//        }
//        return null;
//    }
//}
//

package com.example.demo.layered.service;

import com.example.demo.layered.controller.dto.EmailOtpDto;
import com.example.demo.layered.entity.EmailOtp;
import com.example.demo.layered.mapper.EmailOtpMapper;
import com.example.demo.layered.repository.EmailOtpRepository;
import com.example.demo.layered.usecase.EmailOtpCreateUseCase;
import com.example.demo.layered.usecase.EmailOtpDeleteUseCase;
import com.example.demo.layered.usecase.EmailOtpReadUseCase;
import com.example.demo.layered.usecase.EmailOtpRefreshUseCase;
import com.example.demo.layered.util.SecureRandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailOtpService implements EmailOtpCreateUseCase, EmailOtpDeleteUseCase, EmailOtpReadUseCase, EmailOtpRefreshUseCase {

    private final EmailOtpRepository repository;
    private final EmailOtpMapper mapper;

    @Override
    public EmailOtpDto.Response create(EmailOtpDto.CreateRequest request) {
        EmailOtp emailOtp = mapper.toEntity(request);
        String refreshToken = SecureRandomUtil.generateSecureToken(32);
        emailOtp.setRefreshToken(refreshToken);
        EmailOtp savedEmailOtp = repository.save(emailOtp);
        return mapper.toDto(savedEmailOtp);
    }

    @Override
    public EmailOtpDto.Response read(String id) {
        EmailOtp emailOtp = repository.findById(id)
                .orElseGet(() -> repository.findByEmail(id));
        return mapper.toDto(emailOtp);
    }

    @Override
    public EmailOtpDto.Response delete(String id) {
        EmailOtp emailOtp = repository.findById(id)
                .orElseGet(() -> repository.findByEmail(id));
        if (emailOtp != null) {
            repository.delete(emailOtp);
            return mapper.toDto(emailOtp);
        }
        return null;
    }

    public EmailOtpDto.Response refreshToken(String refreshToken) {
        EmailOtp emailOtp = repository.findByRefreshToken(refreshToken);
        if (emailOtp != null) {
            String newRefreshToken = SecureRandomUtil.generateSecureToken(32);
            emailOtp.setRefreshToken(newRefreshToken);
            EmailOtp savedEmailOtp = repository.save(emailOtp);
            return mapper.toDto(savedEmailOtp);
        }
        return null;
    }

}