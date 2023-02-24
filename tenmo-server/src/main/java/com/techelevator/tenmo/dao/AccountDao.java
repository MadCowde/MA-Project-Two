package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Account;

public interface AccountDao {

    List<Account> findAll();

    Account get(int id);

    boolean remove(int id);

    List<Account> listAccById(int id);

    boolean create(int userId);
}
