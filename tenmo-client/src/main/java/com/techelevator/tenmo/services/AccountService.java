package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.*;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    public static final String API_BASE_URL = "http://localhost:8080/";

    private RestTemplate restTemplate = new RestTemplate();

    private TransferService transferService = new TransferService();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Account getAccount(int userId) {
        Account account = null;
        String url = API_BASE_URL + "accounts/" + userId;

        try {
            ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(),
                    Account.class);
            account = response.getBody();

        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return account;

    }

    public User[] getAllUsers() {
        User[] listOfUsers = null;
        String url = API_BASE_URL + "users";

        try {
            ResponseEntity<User[]> response = restTemplate.exchange(url,
                    HttpMethod.GET, makeAuthEntity(), User[].class);
            listOfUsers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return listOfUsers;
    }

    public Transfer[] getTransferHistory(int currentUserId) {
        Account account = getAccount(currentUserId);

        Transfer[] history = null;
        String url = API_BASE_URL + "transfers/" + currentUserId + "/completed"; // Need to confirm the end point to pull the data.

        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(url,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer[].class);

            history = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return history;
    }

    public Transfer[] getPendingRequests(int currentUserId) {
        //Might be able to use TransferHistory to grab status depending on the endpoint that we use.
        // Goal is  to see pending incoming or outgoing money requests. Potentially need a way to accept money
        Account account = getAccount(currentUserId);

        Transfer[] pending = null;
        String url = API_BASE_URL + "transfers/" + account.getAccount_Id() + "/pending"; //Need to confirm the end point to get

        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(url,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer[].class);

            pending = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return pending;
    }

    public void sendMoney(int sendTo, int sentFrom, BigDecimal amountToSend) {

        Account receiving = getAccount(sendTo);
        Account sending = getAccount(sentFrom);
        if (receiving == null && sendTo == sentFrom) {
            System.out.println("\nUser you are sending money to doesn't exist");
            return;
        }

        Transfer newTransfer = new Transfer(2, sending.getAccount_Id(), receiving.getAccount_Id(), amountToSend);
        boolean isTransferred = false;

        if (sending.getBalance().doubleValue() >= amountToSend.doubleValue() && amountToSend.doubleValue() > 0) {
            isTransferred = transferService.postTransferRequest(newTransfer);
            transferService.processTransfer(receiving, sending, amountToSend, isTransferred);
        } else {
            System.out.println("\n Insufficient funds or invalid input.");
        }

        successOrFail(isTransferred);
    }

    public void requestMoney(int userRequesting, int userRequested, BigDecimal amountToRequest) {
        Account request = getAccount(userRequesting);
        Account requested = getAccount(userRequested);
        if (requested == null && userRequested == userRequesting) {
            System.out.println("The user you have selected does not exist.");
            return;
        }

        if (amountToRequest.doubleValue() > 0) {
            Transfer newTransfer = new Transfer(1,
                    request.getAccount_Id(), requested.getAccount_Id(), amountToRequest);

            successOrFail(transferService.postTransferRequest(newTransfer));
        } else {
            System.out.println("Invalid input. The value must be positive");
        }

    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    public User findUser(String input) {
        // Optional functionality to search for userID/account by String
        String url = API_BASE_URL + "users/" + input; // Need to confirm the end point

        User user = null;

        try {
            ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), User.class);
            user = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return user;
    }

    private void successOrFail(boolean result) {
        if (result) {
            System.out.println("\n The transfer was successful.");
        } else {
            System.out.println("\n The transfer did not complete.");
        }

    }

}
