package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Transfer {

    private int transfer_id;
    @JsonProperty("transfer_type_id")
    private int transfer_type_id;

    private int transfer_status_id;
    @JsonProperty("account_from")
    private int account_from;
    @JsonProperty("account_to")
    private int account_to;
    @JsonProperty("amount")
    private BigDecimal transferAmount;

    public Transfer(){};

    public Transfer(int transfer_type_id, int account_from, int account_to, BigDecimal transferAmount){
        this.transfer_id = transfer_id;
        this.transfer_type_id = transfer_type_id;
        this.account_from = account_from;
        this.account_to = account_to;
        this.transferAmount = transferAmount;
        switch (transfer_type_id) {
            case (1):
                this.transfer_status_id = 1;
                break;
            case (2):
                this.transfer_status_id = 2;
                break;
            default:
                this.transfer_status_id = 0;
        }
    }
    public Transfer(int transfer_id, int transfer_type_id, int account_from, int account_to, BigDecimal transferAmount){
        this.transfer_id = transfer_id;
        this.transfer_type_id = transfer_type_id;
        this.account_from = account_from;
        this.account_to = account_to;
        this.transferAmount = transferAmount;
        switch (transfer_type_id) {
            case (1):
                this.transfer_status_id = 1;
                break;
            case (2):
                this.transfer_status_id = 2;
                break;
            default:
                this.transfer_status_id = 0;
        }
    }

  /*  public Transfer(int transfer_type_id, int transfer_status_id, int account_from, int account_to, BigDecimal transferAmount) {
        this.transfer_type_id = transfer_type_id;
        this.transfer_status_id = transfer_status_id;
        this.account_from = account_from;
        this.account_to = account_to;
        this.transferAmount = transferAmount;
    } */

    public int getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(int transfer_id) {
        this.transfer_id = transfer_id;
    }

    public int getTransfer_type_id() {
        return transfer_type_id;
    }

    public void setTransfer_type_id(int transfer_type_id) {
        this.transfer_type_id = transfer_type_id;
    }

    public int getTransfer_status_id() {
        return transfer_status_id;
    }

    public void setTransfer_status_id(int transfer_status_id) {
        this.transfer_status_id = transfer_status_id;
    }

    public int getAccount_from() {
        return account_from;
    }

    public void setAccount_from(int account_from) {
        this.account_from = account_from;
    }

    public int getAccount_to() {
        return account_to;
    }

    public void setAccount_to(int account_to) {
        this.account_to = account_to;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String toString(){
        return "TransferId= " + transfer_id + ", TransferStatus= " + transfer_status_id + ", TransferType= "
                + transfer_type_id;
    }
}
