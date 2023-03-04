package com.techelevator.dao;

import com.techelevator.tenmo.dao.Data;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;

public class JdbcAccountDaoTests extends Data {
    private Account testAccount;
    private JdbcAccountDao sut;

    protected static final User USER_1 = new User(1001, "user1", "user1", "USER");
    protected static final User USER_2 = new User(1002, "user2", "user2", "USER");
    private static final User USER_3 = new User(1003, "user3", "user3", "USER");

    private static final Account ACCOUNT_1 = new Account(2001, USER_1.getId(),new BigDecimal("1000.00"));
    private static final Account ACCOUNT_2 = new Account(2002, USER_2.getId(), new BigDecimal("1000.00"));
    private static final Account ACCOUNT_3 = new Account(2003, USER_3.getId(), new BigDecimal("1000.00"));
    @Before
    public void setup() {
        sut = new JdbcAccountDao();
    }

    @Test(expected = IllegalArgumentException.class)
    public void findAccountByUsername_given_null_throws_exception(){
        sut.get(null);
    }

    @Test
    public void get_account_by_userId(){
//        Account actualAcct = sut.get(ACCOUNT_1.getAccount_Id());
        Account acctual = sut.get(Integer.toString(USER_1.getId()));
        assertAccountsMatch(ACCOUNT_1,acctual);
    }

    private void assertAccountsMatch(Account expected, Account actual) {
        Assert.assertEquals(expected.getAccount_Id(), actual.getAccount_Id());
        Assert.assertEquals(expected.getUser_Id(), actual.getUser_Id());
        Assert.assertEquals(expected.getBalance(), actual.getBalance());
    }
    //    @AfterEach
//    @AfterAll
//    public void shutdown() {
//        JdbcTemplate jd = new Data().getJdbcTemplate();
//        jd.update("DELETE FROM account;");
//        jd.update("DELETE FROM tenmo_user;");
//        jd.update("alter sequence seq_account_id  restart with 2001;");
//        jd.update("alter sequence seq_user_id  restart with 1001;");
//
//    }


}
