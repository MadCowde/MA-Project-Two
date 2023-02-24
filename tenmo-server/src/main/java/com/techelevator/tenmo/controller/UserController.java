package com.techelevator.tenmo.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.JdbcUserDao;

@PreAuthorize("isAuthenticated()")
@RequestMapping("/users/")
@RestController
public class UserController {
    JdbcUserDao userDao = new JdbcUserDao(new JdbcTemplate());

}
