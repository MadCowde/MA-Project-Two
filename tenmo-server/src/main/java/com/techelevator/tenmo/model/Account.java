package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {
    int account_Id;
    int user_Id;
    BigDecimal balance;

    public Account(int accId, int userId, BigDecimal balance) {
        this.account_Id = accId;
        this.user_Id = userId;
        this.balance = balance;
    }

    public int getAccount_Id() {
        return account_Id;
    }

    public void setAccount_Id(int account_Id) {
        this.account_Id = account_Id;
    }

    public int getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(int user_Id) {
        this.user_Id = user_Id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
