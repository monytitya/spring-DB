package com.example.spring_db.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final DataSource datasource;

    public ProductController(DataSource datasource) {
        this.datasource = datasource;
    }

    @PostMapping("/create")
    public String create(String name, float price) {
        String sql = "INSERT INTO products(name, price) VALUES(?, ?)";

        try (Connection conn = datasource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setFloat(2, price);
            ps.executeUpdate();

            return "User added!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @GetMapping("{id}")
    public List<Map<String, Object>> getAll() {
        List<Map<String, Object>> users = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (Connection conn = datasource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            // Why we use while?? ,Because we want to get all the users from the database.
            while (rs.next()) {
                Map<String, Object> user = new HashMap<>();
                user.put("name", rs.getString("name"));
                user.put("price", rs.getInt("price"));
                users.add(user);
            }
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            users.add(error);
        }
        return users;
    }

    // UPDATE
    @PutMapping("/update")
    public Map<String, Object> update(@RequestParam int id, @RequestParam String name, @RequestParam float price) {
        Map<String, Object> res = new HashMap<>();
        String sql = "UPDATE products SET name = ?, price = ? WHERE id = ?";

        try (Connection conn = datasource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setFloat(2, price);
            ps.setInt(3, id);

            int updatedRows = ps.executeUpdate();
            if (updatedRows > 0) {
                res.put("status", "success");
                res.put("message", "user updated");
            } else {
                res.put("status", "fail");
                res.put("message", "user not found");
            }
        } catch (Exception e) {
            res.put("status", "error");
            res.put("message", e.getMessage());
        }

        return res;

    }

    @DeleteMapping("/delete")
    public String delete(int id) {
        String sql = "DELETE FROM products WHERE id=?";
        try (Connection conn = datasource.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

            return "Deleted!";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
