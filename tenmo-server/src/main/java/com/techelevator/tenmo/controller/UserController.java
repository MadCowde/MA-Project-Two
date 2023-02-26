package com.techelevator.tenmo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;

//@PreAuthorize("isAuthenticated()")
@RequestMapping("/users")
@RestController
public class UserController {
    @Autowired
    JdbcUserDao userDao;

    @GetMapping("")
    public List<User> list() {
        return userDao.findAll("Pull");
    }

    @GetMapping("{input}")
    public User get(@PathVariable String input) {
        return userDao.getUser(input);
    }
}
