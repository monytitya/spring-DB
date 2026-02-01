package com.example.spring_db.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControllers {

    // private final DataSource dataSource;
    // public UserControllers(DataSource dataSource) {
    // this.dataSource = dataSource;
    // }
    // @GetMapping("/test1")
    // public String testdb() {
    // try (Connection connection = dataSource.getConnection()) {
    // return " DB successfully connected";
    // } catch (Exception e) {
    // return " DB connection failed: " + e.getMessage();
    // }
    // }

    // Map store key and value
    private final DataSource datasource;

    public UserControllers(DataSource datasource) {
        this.datasource = datasource;

    }

    @PostMapping("/create")
    public Map<String, Object> create(@RequestParam String name, @RequestParam int age) {
        Map<String, Object> res = new HashMap<>();
        // query to insert data into database
        String sql = "INSERT INTO users (name, age) VALUES (?, ?)";

        try (Connection conn = datasource.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, age);
            ps.executeUpdate();

            res.put("status", "success");
            res.put("message", "user created");
        } catch (Exception e) {
            res.put("message", "error");
            res.put("db", e.getMessage());

        }
        return res;
    }

}
