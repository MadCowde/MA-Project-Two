package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import com.techelevator.tenmo.model.Account;

@Component
public class JdbcAccountDao implements AccountDao {

    private static final BigDecimal STARTING_BALANCE = new BigDecimal("1000.00");
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Account> listAccById(int id) {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM account WHERE user_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
        while (result.next()) {
            accounts.add(mapRowToAccount(result));
        }
        return accounts;
    }

    @Override
    public boolean remove(int id) {
        String sql = "SELECT * FROM account where account_id = ?";
        if (jdbcTemplate.queryForObject(sql, Account.class, id) != null) {
            sql = "DELETE FROM account WHERE account_id = ?";
            jdbcTemplate.update(sql, id);
            return true;
        }
        return false;
    }

    @Override
    public Account get(int id) {
        String sql = "SELECT * FROM account where account_id = ?";
        Account acc = jdbcTemplate.queryForObject(sql, Account.class, id);
        return acc;
    }

    @Override
    public boolean create(int newUserId) {
        String sql = "INSERT INTO account (user_id, balance) values (?, ?);";
        try {
            jdbcTemplate.update(sql, newUserId, STARTING_BALANCE);
        } catch (DataAccessException e) {
            System.out.println("update didn't work.");
            return false;
        }
        return true;
    }

    @Override
    public List<Account> listAll() {
        List<Account> acc = new ArrayList<>();
        String sql = "SELECT * FROM account";
        acc = jdbcTemplate.queryForList(sql, Account.class);
        return acc;
    }

    private Account mapRowToAccount(SqlRowSet result) {
        Account account = new Account(result.getInt("account_id"), result.getInt("user_id"),
                result.getBigDecimal("balance"));
        return account;

    }

}
