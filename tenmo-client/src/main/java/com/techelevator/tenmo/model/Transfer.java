package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int transfer_id;

    private int transfer_type_id;

    private int transfer_status_id;

    private int account_from;

    private int account_to;

    private BigDecimal transferAmount;

    public Transfer(){};

    public Transfer(int transfer_type_id, int transfer_status_id, int account_from, int account_to, BigDecimal transferAmount) {
        this.transfer_type_id = transfer_type_id;
        this.transfer_status_id = transfer_status_id;
        this.account_from = account_from;
        this.account_to = account_to;
        this.transferAmount = transferAmount;
    }
}
