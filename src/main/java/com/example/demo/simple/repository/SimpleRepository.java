package com.example.demo.simple.repository;

import com.example.demo.simple.entity.Simple;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimpleRepository extends CrudRepository<Simple, String> {

}
