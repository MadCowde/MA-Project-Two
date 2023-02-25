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
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, id);
        if (rs.next()) {
            return new Account(rs.getInt("account_id"), rs.getInt("user_id"), rs.getBigDecimal("balance"));
        }
        return null;

    }

    //@Override
//    public Account getById(int id) {
//        //Potentially need to use the User name from principal to find an ID > getBalance on front end
//        Account account = null;
//        String sql = "SELECT account.account_id, account.user_id, account.balance FROM tenmo_user\n" +
//                "JOIN account ON account.user_id = tenmo_user.user_id\n" +
//                "WHERE tenmo_user.user_id = ?;";
//        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);
//
//        if (result.next()){
//            account = mapRowToAccount(result);
//        }
//        return account;
//
 //   }

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
    public Account create(Account acc) {
        String sql = "INSERT INTO account (user_id, balance) values (?, ?) RETURNING account_id;";
        try {
            int id = jdbcTemplate.update(sql, acc.getUser_Id(), STARTING_BALANCE);
            acc.setAccount_Id(id);
        } catch (DataAccessException e) {
            System.out.println("update didn't work.");
            return null;
        }
        return acc;
    }

    @Override
    public List<Account> listAll() {
        List<Account> acc = new ArrayList<>();
        String sql = "SELECT * FROM account";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql);
        while (rs.next()) {
            acc.add(new Account(rs.getInt("account_id"), rs.getInt("user_id"), rs.getBigDecimal("balance")));
        }

        return acc;
    }

    private Account mapRowToAccount(SqlRowSet result) {
        Account account = new Account(result.getInt("account_id"), result.getInt("user_id"),
                result.getBigDecimal("balance"));
        return account;

    }

}
