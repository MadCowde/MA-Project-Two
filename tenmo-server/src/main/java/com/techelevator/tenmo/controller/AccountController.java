package com.techelevator.tenmo.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    @GetMapping("/{account_Id}")
    public Account get(Principal princ, @PathVariable int account_Id) {
        Account acc = accDao.get(account_Id);

        return acc;
    }
    @GetMapping("/{id}")
    public Account getByUserId(@PathVariable int id) {
        //API to get user by user_id
        Account acc = accDao.getById(id);
        if(acc == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account is not found.");
        } else {
            return acc;
        }

    }

    @GetMapping("")
    public List<Account> allAccounts() {
        List<Account> all = accDao.listAll();
        return all;
    }

    @PostMapping("")
    public Account create(@RequestBody @Valid Account acc) throws Exception {
        if (!(acc == null)) {
            return accDao.create(acc);
        }
        throw new Exception();
    }
}
