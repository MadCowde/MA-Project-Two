package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class JdbcUserDao implements UserDao {
    private static List<User> users = new ArrayList<>();
    private final String USER = "USER";
    private JdbcTemplate jd;
    @Autowired
    JdbcAccountDao accDao = new JdbcAccountDao();

    public JdbcUserDao(JdbcTemplate jt) {
        this.jd = jt;
    }

    public JdbcUserDao() {
        this.jd = new Data().getJdbcTemplate();
    }

    @Override
    public int getId(String input) {
        return getUser(input).getId();
    }

    /*
    * Takes in an account Id, User Id, or a username and returns the User object that is connected to one of those 
    * if it has already been created. Returns null otherwise.
    */
    @Override
    public User getUser(String input) {
        if (Objects.isNull(input) || input.isBlank())
            throw new IllegalArgumentException("invalid input.");
        switch (inputType(input)) {
            case ("Account"):
                int id = Integer.parseInt(input);
                String sql = "SELECT * FROM tenmo_user t JOIN account a ON t.user_id = a.user_id WHERE account_id = ?";
                SqlRowSet rs = jd.queryForRowSet(sql, id);
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
                throw new UsernameNotFoundException("The account ID: " + input + " was not found.");
            case ("User"):
                id = Integer.parseInt(input);
                sql = "SELECT * FROM tenmo_user where user_id = ?";
                rs = jd.queryForRowSet(sql, id);
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
                throw new UsernameNotFoundException("The User ID: " + input + " was not found.");
            case ("Username"):
                sql = "SELECT t.user_id, t.username, t.password_hash, t.role FROM account a JOIN tenmo_user t ON a.user_id = t.user_id where username = ?;";
                rs = jd.queryForRowSet(sql, input);
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
                throw new UsernameNotFoundException("The username: " + input + " was not found.");

            default:
                return null;
        }
    }

    /*
     * Assigns all User objects that are already in the database into a List.
     */
    @Override
    public List<User> findAll(String input) {
        boolean isEqual = false;
        switch (input) {
            case ("Fill"):
                String sql = "SELECT user_id, username, password_hash, role FROM tenmo_user";
                SqlRowSet results = jd.queryForRowSet(sql);
                while (results.next()) {
                    User user = mapRowToUser(results);
                    for (User u : users) {
                        if (u.equals(user)) {
                            isEqual = true;
                            break;
                        } else {
                            isEqual = false;
                        }
                    }
                    if (isEqual) {
                        continue;
                    } else {
                        users.add(user);
                    }
                }
                return users;
            case ("Pull"):
                return users;
            default:
                return users;
        }
    }

    @Override
    public boolean create(String username, String password) {
        findAll("Fill");
        if (Objects.isNull(username) || username.isBlank())
            throw new DataIntegrityViolationException("Null or empty usernames not allowed.");
        if (Objects.isNull(password) || password.isBlank())
            throw new java.lang.IllegalArgumentException("Null or empty passwords not allowed.");
        for (User u : users) {
            try {
                if (u.getUsername().equals(username)) {
                    System.out.println("Username " + username + " already exists.");
                    throw new DataIntegrityViolationException("Username already exists.");
                }
            } catch (DataIntegrityViolationException e) {
                System.out.println(e.getMessage());
                return false;
            } catch (UsernameNotFoundException e) {
                // create user
                String sql = "INSERT INTO tenmo_user (username, password_hash, role) VALUES (?, ?, ?) RETURNING user_id;";
                String password_hash = new BCryptPasswordEncoder().encode(password);
                Integer newUserId;
                newUserId = jd.queryForObject(sql, Integer.class, username, password_hash, USER);

                if (newUserId == null)
                    return false;
                // create account
                System.out.println("Created " + username);
                User newUser = new User();
                newUser.setActivated(true);
                newUser.setAuthorities(USER);
                newUser.setId(newUserId);
                newUser.setUsername(username);
                newUser.setPassword(password_hash);
                users.add(newUser);
                accDao.create(newUserId);
                return true;
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        // create user
        String sql = "INSERT INTO tenmo_user (username, password_hash, role) VALUES (?, ?, ?) RETURNING user_id;";
        String password_hash = new BCryptPasswordEncoder().encode(password);
        Integer newUserId;
        newUserId = jd.queryForObject(sql, Integer.class, username, password_hash, USER);

        if (newUserId == null)
            return false;
        // create account
        System.out.println("Created " + username);
        User newUser = new User();
        newUser.setActivated(true);
        newUser.setAuthorities(USER);
        newUser.setId(newUserId);
        newUser.setUsername(username);
        newUser.setPassword(password_hash);
        users.add(newUser);
        accDao.create(newUserId);
        return true;
    }

    public boolean remove(User user) {
        try {
            if (!Objects.isNull(getUser(user.getUsername()))) {
                if (users.remove(user)) {
                    String sql = "DELETE FROM account WHERE user_id IN (SELECT user_id FROM tenmo_user WHERE username = ?)";
                    jd.update(sql, user.getUsername());
                    sql = "DELETE FROM tenmo_user WHERE username = ?;";
                    jd.update(sql, user.getUsername());
                    return true;
                }
            }
        } catch (UsernameNotFoundException e) {
            return false;
        }
        return false;

    }

    public String inputType(String input) {
        try {
            if (Integer.parseInt(input) / 1000 == 2) {
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

    public User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities(rs.getString("role"));
        return user;
    }
}
