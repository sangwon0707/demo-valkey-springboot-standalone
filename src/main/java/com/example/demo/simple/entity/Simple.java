package com.example.demo.simple.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("simple") //K-V DB, Key의 앞에 prefix로 붙이는 문자열
public class Simple {
    @Id
    @Getter
    private String id;
    public String name;
    public Integer age;
    @TimeToLive //데이터의 수명 (TTL) unit: [sec]
    public Integer ttl;
}
