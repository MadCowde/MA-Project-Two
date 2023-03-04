package com.techelevator.tenmo.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import com.techelevator.tenmo.dao.JdbcTransferDao;

public class Transfer {
    int from;
    int to;
    BigDecimal amount;
    int status;
    int type;
    int id;
    JdbcTransferDao transDao = new JdbcTransferDao();

    public Transfer(int type, int from, int to, BigDecimal amount) {
        this.id = 0;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.type = type;
        switch (type) {
            case (1):
                this.status = 1;
                break;
            case (2):
                this.status = 2;
                break;
            default:
                this.status = 0;
        }
    }

    public Transfer() {
    }

    public boolean equals(Transfer t) {
        if (!(t.getId() == this.id)) {
            return false;
        }
        if (!(t.getFrom() == (this.from))) {
            return false;
        }
        if (!(t.getTo() == (this.to))) {
            return false;
        }
        if (!(t.getStatus() == this.status)) {
            return false;
        }
        if (!(t.getType() == this.type)) {
            return false;
        }
        return true;
    }

    public @NotBlank int getFrom() {
        return from;
    }

    public void setFrom(@NotBlank int from) {
        this.from = from;
    }

    public @NotBlank int getTo() {
        return to;
    }

    public void setTo(@NotBlank int to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
