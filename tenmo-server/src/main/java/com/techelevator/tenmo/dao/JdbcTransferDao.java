package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

public class JdbcTransferDao implements TransferDao {
    public static List<Transfer> transfers = new ArrayList<Transfer>();
    JdbcAccountDao accDao = new JdbcAccountDao();
    JdbcTemplate jt;

    public JdbcTransferDao(JdbcTemplate jt) {
        this.jt = jt;
    }

    public JdbcTransferDao() {
        this.jt = new Data().getJdbcTemplate();
    }

    @Override
    public List<Transfer> getAll(String input) {
        boolean isEqual = false;
        switch (input) {
            case ("Fill"):
                String sql = "SELECT * FROM transfer";
                SqlRowSet results = jt.queryForRowSet(sql);
                while (results.next()) {
                    Transfer transfer = mapRowToTransfer(results);
                    for (Transfer t : transfers) {
                        if (t.equals(transfer)) {
                            isEqual = true;
                            break;
                        } else {
                            isEqual = false;
                        }
                    }
                    if (isEqual) {
                        continue;
                    } else {
                        transfers.add(transfer);
                    }
                }
                return transfers;
            case ("Pull"):
                return transfers;
            default:
                return transfers;
        }
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer trans = new Transfer();
        trans.setId(results.getInt("transfer_id"));
        trans.setAmount(results.getBigDecimal("amount"));
        trans.setFrom(results.getInt("account_from"));
        trans.setTo(results.getInt("account_to"));
        trans.setType(results.getInt("transfer_type_id"));
        trans.setStatus(results.getInt("transfer_status_id"));
        return trans;

    }

    public List<Transfer> get(String input) {
        List<Transfer> listing = new ArrayList<Transfer>();
        if (Objects.isNull(input) || input.isBlank())
            throw new IllegalArgumentException("invalid input.");
        switch (inputType(input)) {
            case ("Transfer"):
                int id = Integer.parseInt(input);
                String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
                SqlRowSet rs = jt.queryForRowSet(sql, id);
                if (rs.next()) {
                    listing.add(mapRowToTransfer(rs));
                    return listing;
                }
                throw new UsernameNotFoundException("The transfer ID: " + input + " was not found.");
            case ("Account"):
                id = Integer.parseInt(input);
                sql = "SELECT * FROM transfer t JOIN account a ON t.account_from = a.account_id WHERE account_id = ?";
                rs = jt.queryForRowSet(sql, id);
                while (rs.next()) {
                    listing.add(mapRowToTransfer(rs));
                }
                sql = "SELECT * FROM transfer t JOIN account a ON t.account_to = a.account_id WHERE account_id = ?";
                rs = jt.queryForRowSet(sql, id);
                while (rs.next()) {
                    listing.add(mapRowToTransfer(rs));
                }
                if (!(listing.isEmpty())) {
                    return listing;
                }
                throw new UsernameNotFoundException("The account ID: " + input + " was not found.");
            case ("User"):
                id = Integer.parseInt(input);
                sql = "select u.user_id, t.transfer_id, t.amount, t.account_from, t.account_to, t.transfer_type_id,t.transfer_status_id from tenmo_user u join (account a join transfer t ON t.account_from = a.account_id) ON a.user_id = u.user_id WHERE a.user_id = ?;";
                rs = jt.queryForRowSet(sql, id);
                while (rs.next()) {
                    listing.add(mapRowToTransfer(rs));
                }
                sql = "select u.user_id, t.transfer_id, t.amount, t.account_from, t.account_to, t.transfer_type_id,t.transfer_status_id from tenmo_user u join (account a join transfer t ON t.account_to = a.account_id) ON a.user_id = u.user_id where a.user_id = ?;";
                rs = jt.queryForRowSet(sql, id);
                while (rs.next()) {
                    listing.add(mapRowToTransfer(rs));
                }
                if (!(listing.isEmpty())) {
                    return listing;
                }
                throw new UsernameNotFoundException("The User ID: " + input + " was not found.");
            case ("Username"):
                sql = "select u.user_id, t.transfer_id, t.amount, t.account_from, t.account_to, t.transfer_type_id,t.transfer_status_id from tenmo_user u join (account a join transfer t ON t.account_to = a.account_id) ON a.user_id = u.user_id where u.username = ?;";
                rs = jt.queryForRowSet(sql, input);
                while (rs.next()) {
                    listing.add(mapRowToTransfer(rs));
                }
                sql = "select u.user_id, t.transfer_id, t.amount, t.account_from, t.account_to, t.transfer_type_id,t.transfer_status_id from tenmo_user u join (account a join transfer t ON t.account_to = a.account_id) ON a.user_id = u.user_id where u.username = ?;";
                rs = jt.queryForRowSet(sql, input);
                while (rs.next()) {
                    listing.add(mapRowToTransfer(rs));
                }
                if (!(listing.isEmpty())) {
                    return listing;
                }
                throw new UsernameNotFoundException("The username: " + input + " was not found.");

            default:
                return null;
        }
    }

    public String inputType(String input) {
        try {
            if (Integer.parseInt(input) / 1000 == 3) {
                return "Transfer";
            } else if (Integer.parseInt(input) / 1000 == 2) {
                return "Account";
            } else if (Integer.parseInt(input) / 1000 == 1) {
                return "User";
            } else
                return "null";
        } catch (NumberFormatException e) {
            return "Username";
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean create(int type, int from, int to, BigDecimal amt) {
        if ((type == 1 || type == 2) && !Objects.isNull(accDao.get(Integer.toString(from)))
                && !Objects.isNull(accDao.get(Integer.toString(to)))
                && (amt.compareTo(new BigDecimal(0)) > 0)) {
            Transfer trans = new Transfer(type, from, to, amt);
            String sql = "INSERT INTO transfer (transfer_type_id,transfer_status_id,account_from,account_to, amount) values (?, ?, ?, ?, ?) RETURNING transfer_id;";
            trans.setId(jt.queryForObject(sql, Integer.class, type, type, from, to, amt));
            transfers.add(trans);
            return true;
        } else {
            return false;
        }
    }

    public List<Transfer> getPending(Account acc) {
        List<Transfer> listing = getAll("Pull");
        for (Transfer t : listing) {
            if (!(t.getTo() == acc.getAccount_Id())) {
                listing.remove(t);
            }
        }
        return listing;
    }

    public boolean remove(int id) {
        Transfer trans = get(Integer.toString(id)).get(0);
        if (transfers.contains(trans)) {
            jt.update("DELETE FROM transfer WHERE transfer_id = ?", trans.getId());
            transfers.remove(trans);
            return true;
        }
        return false;
    }

}
