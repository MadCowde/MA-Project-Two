package com.techelevator.tenmo.controller;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;

@PreAuthorize("isAuthenticated()")
@RequestMapping("/users/")
@RestController
public class UserController {
    JdbcUserDao userDao = new JdbcUserDao(new JdbcTemplate());

    @GetMapping()
    public List<User> list() {
        return userDao.findAll();
    }

    @GetMapping("{id}")
    public User get(@RequestParam int id) {
        return userDao.getUserById(id);
    }
}
