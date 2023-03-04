package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;
import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {

    List<Transfer> getAll(String input);

    List<Transfer> get(String input);

    int create(int type, int from, int to, BigDecimal amt);
}
