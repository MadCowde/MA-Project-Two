package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.web.server.ResponseStatusException;

@RestController
//@PreAuthorize("isAuthenticated()")
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    JdbcAccountDao accDao = new JdbcAccountDao(new JdbcTemplate());

    //@PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public Account getAccount(@PathVariable int id) {
        Account acc = accDao.get(id);
        if(acc == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account is not found.");
        } else {
            return acc;
        }
    }
}
