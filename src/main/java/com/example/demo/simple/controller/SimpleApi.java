package com.example.demo.simple.controller;

import com.example.demo.simple.entity.Simple;
import com.example.demo.simple.repository.SimpleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/simple")
@RequiredArgsConstructor
public class SimpleApi {

    private final SimpleRepository repository;

    @GetMapping("/{key}")
    public Simple getStandalone(@PathVariable String key) {

        return repository.findById(key)
                .orElse(null);
        //suggested: .orElseThrow( ... );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Simple saveStandalone(@RequestBody Simple simple){
        return repository.save(simple);
    }
}
