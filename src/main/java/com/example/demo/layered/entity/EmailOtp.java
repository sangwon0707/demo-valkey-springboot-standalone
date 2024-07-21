package com.example.demo.layered.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("otp")
@Getter
@Setter
public class EmailOtp {
    @Id
    @Getter
    private String id;
    @Indexed //(cluster 환경) 조회조건(id) = email로 id를 조회한다.
    public String email;
    public String otp;
    private String refreshToken;
    @TimeToLive //데이터의 수명 (TTL) unit: [sec]
    public Integer ttl;

}
