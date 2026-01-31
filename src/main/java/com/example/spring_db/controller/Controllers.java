package com.example.spring_db.controller;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controllers {
    private final DataSource dataSource;

    public Controllers(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/test")
    public String testdb() {
        try (Connection connection = dataSource.getConnection()) {
            return " DB successfully connected";
        } catch (Exception e) {
            return " DB connection failed: " + e.getMessage();
        }
    }
}
