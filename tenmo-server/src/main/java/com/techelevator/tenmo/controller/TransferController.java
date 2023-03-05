package com.techelevator.tenmo.controller;

import java.util.List;
import java.util.Objects;
import java.util.zip.DataFormatException;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Transfer;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    JdbcUserDao userDao = new JdbcUserDao();
    JdbcAccountDao accDao = new JdbcAccountDao();
    JdbcTransferDao transDao = new JdbcTransferDao();

    @GetMapping("/{input}")
    public List<Transfer> get(@PathVariable String input) {
        List<Transfer> trans = transDao.get(input);
        return trans;
    }

    @GetMapping("")
    public List<Transfer> allAccounts() {
        List<Transfer> all = transDao.getAll("Fill");
        return all;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean create(@RequestBody @Valid Transfer trans) throws DataFormatException {
        if (!(Objects.isNull(trans))) {
            return transDao.create(trans.getType(), trans.getFrom(), trans.getTo(), trans.getAmount());
        }
        throw new DataFormatException("Wrong data format.");
    }

    @GetMapping("/{input}/pending")
    public List<Transfer> getPending(@PathVariable String input) {
        return transDao.getPending(accDao.get(input));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public boolean remove(@PathVariable int id) {
        return transDao.remove(id);
    }

    @PutMapping("/{input}/pending/{id}")
    public boolean setStatus(Transfer trans) {
        Transfer changed = transDao.get(Integer.toString(trans.getId())).get(0);
        changed.setStatus(trans.getStatus());
        return true;
    }
}
