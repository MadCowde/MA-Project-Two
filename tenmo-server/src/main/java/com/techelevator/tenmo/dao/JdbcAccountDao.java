package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import com.techelevator.tenmo.model.Account;

@Component
public class JdbcAccountDao implements AccountDao {

    private static final BigDecimal STARTING_BALANCE = new BigDecimal("1000.00");

    private final JdbcTemplate jt;

    public JdbcAccountDao(JdbcTemplate jt) {
        this.jt = jt;
    }

    public JdbcAccountDao() {
        this.jt = new Data().getJdbcTemplate();
    }

    // @Override
    // public boolean remove(int id) {
    //     String sql = "SELECT * FROM account where account_id = ?";
    //     if (jdbcTemplate.queryForObject(sql, Account.class, id) != null) {
    //         sql = "DELETE FROM account WHERE account_id = ?";
    //         jdbcTemplate.update(sql, id);
    //         return true;
    //     }
    //     return false;
    // }

    public Account get(String username) {
        if (username == null)
            return null;
        try {
            if (Integer.parseInt(username) / 1000 == 2) {
                int id = Integer.parseInt(username);
                String sql = "SELECT * FROM account where account_id = ?";
                SqlRowSet rs = jt.queryForRowSet(sql, id);
                if (rs.next()) {
                    return new Account(rs.getInt("account_id"), rs.getInt("user_id"), rs.getBigDecimal("balance"));
                }
                return null;
            } else {
                int id = Integer.parseInt(username);
                String sql = "SELECT * FROM account where user_id = ?";
                SqlRowSet rs = jt.queryForRowSet(sql, id);
                if (rs.next()) {
                    return new Account(rs.getInt("account_id"), rs.getInt("user_id"), rs.getBigDecimal("balance"));
                }
                return null;
            }
        } catch (Exception e) {
            String sql = "SELECT a.account_id, a.user_id, a.balance FROM account a JOIN tenmo_user t ON a.user_id = t.user_id where username = ?;";
            SqlRowSet rs = jt.queryForRowSet(sql, username);
            if (rs.next()) {
                return new Account(rs.getInt("account_id"), rs.getInt("user_id"), rs.getBigDecimal("balance"));
            }
            return null;
        }

    }

    @Override
    public boolean create(int newUserId) {
        String sql = "INSERT INTO account (user_id, balance) values (?, ?);";
        try {
            jt.update(sql, newUserId, STARTING_BALANCE);
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
            int id = jt.update(sql, acc.getUser_Id(), STARTING_BALANCE);
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
        SqlRowSet rs = jt.queryForRowSet(sql);
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
