package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/Accounts/")
public class AccountController {

    JdbcAccountDao accDao = new JdbcAccountDao(new JdbcTemplate());

    //@PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("{accId}")
    public BigDecimal getBalance(Principal princ, @RequestParam int accId) {
        Account acc = accDao.get(accId);
        return acc.getBalance();
    }
}
