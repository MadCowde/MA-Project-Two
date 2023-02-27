package com.techelevator.tenmo.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;

//@PreAuthorize("isAuthenticated()")
@RequestMapping("/users")
@RestController
public class UserController {

    JdbcUserDao userDao = new JdbcUserDao();

    @GetMapping("")
    public List<User> list() {

        return userDao.findAll("Fill");
    }

    @GetMapping("/{input}")
    public User get(@PathVariable String input) {
        return userDao.getUser(input);
    }
}
