package com.example.demo.layered.repository;

import com.example.demo.layered.entity.EmailOtp;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailOtpRepository extends CrudRepository<EmailOtp, String> {
    EmailOtp findByRefreshToken(String refreshToken);
}
