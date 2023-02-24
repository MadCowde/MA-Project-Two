package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.tenmo.model.Account;

public class JdbcAccountDao implements AccountDao {

    private static final BigDecimal STARTING_BALANCE = new BigDecimal("1000.00");
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Account> listAccById(int id) {
        return null;
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
        String sql = "INSERT INTO account (user_id, balance) values(?, ?)";
        try {
            jdbcTemplate.update(sql, newUserId, STARTING_BALANCE);
        } catch (DataAccessException e) {
            return false;
        }
        return true;
    }

    @Override
    public List<Account> findAll() {
        return null;
    }

}
