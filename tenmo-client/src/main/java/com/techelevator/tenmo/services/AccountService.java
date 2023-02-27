package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;


public class AccountService {

    public static final String API_BASE_URL = "http://localhost:8080/";

    private RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Account getAccount(int userId){
        Account account = null;
        String url = API_BASE_URL + "accounts/" + userId;

        try{
            ResponseEntity<Account> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }

       return account;

    }
   public int findUserId(String name){
        //Goal of this function is to use principal getName to find a user_id on API. Optional functionality to search for userID/account by String
        String url = API_BASE_URL + ""; // Need to confirm the end point

        int userId = 0;

        try {
            ResponseEntity<Integer> response = restTemplate.exchange(url, HttpMethod.GET, makeAuthEntity(), int.class);
            userId = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return userId;
    }

      public Transfer[] getTransferHistory(int currentUserId){
        Account account = getAccount(currentUserId);

        Transfer[] history = null;
        String url = API_BASE_URL +"" + account.getAccount_Id(); // Need to confirm the end point to pull the data.

        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(url,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer[].class);

            history = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }

        return history;
      }

      public Transfer[] getPendingRequests(int currentUserId){
        //Might be able to use TransferHistory to grab status depending on the endpoint that we use.
        // Goal is  to see pending incoming or outgoing money requests. Potentially need a way to accept money
          Account account = getAccount(currentUserId);

            Transfer[] pending = null;
            String url = API_BASE_URL + "" + account.getAccount_Id(); //Need to confirm the end point to get

          try {
              ResponseEntity<Transfer[]> response = restTemplate.exchange(url,
                      HttpMethod.GET,
                      makeAuthEntity(),
                      Transfer[].class);

              pending = response.getBody();
          } catch (RestClientResponseException | ResourceAccessException e){
              BasicLogger.log(e.getMessage());
          }

          return pending;
      }

      public void sendMoney(int sendTo, int sentFrom, BigDecimal amountToSend){
        Account receiving = getAccount(sendTo);
        Account sending = getAccount(sentFrom);
        BigDecimal add = receiving.getBalance().add(amountToSend);
        BigDecimal subtract = sending.getBalance().subtract(amountToSend);

        //Need to post these objects to database after the pending request has been approved.
        receiving.setBalance(add);
        sending.setBalance(subtract);

        //Need to be able to set the status of the request to pending and the person who receives the money has to accept it.


      }

      public void requestMoney(int userRequesting , int userRequested , BigDecimal amountToRequest){
        Account request = getAccount(userRequesting);
        Account requesting = getAccount(userRequested);

        BigDecimal add = request.getBalance().add(amountToRequest);
        BigDecimal subtract = requesting.getBalance().subtract(amountToRequest);

        //Need to post these objects to database after the pending request has been approved
        request.setBalance(add);
        requesting.setBalance(subtract);

        //Need to be able to set the status of the request to pending until the person who is receiving the request approves it.


      }



    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
