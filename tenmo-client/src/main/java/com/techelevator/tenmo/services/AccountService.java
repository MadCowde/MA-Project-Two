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

    public static final String API_BASE_URL = "http://localhost:8080/accounts/";

    private RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public BigDecimal getBalance(int userId){
        Account account = null;
        try{
            ResponseEntity<Account> response = restTemplate.exchange(API_BASE_URL + userId, HttpMethod.GET, makeAuthEntity(), Account.class);
            account = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }

       return account.getBalance();

    }
//    private int findUserId(String name){
//        //Goal of this function is to use principal getName to find a user_id on API.
//        String API_BASE_URL_TWO = "http://localhost:8080/";
//
//        int userId = 0;
//
//        try {
//            ResponseEntity<Integer> response = restTemplate.exchange(API_BASE_URL_TWO, HttpMethod.GET, makeAuthEntity(), int.class);
//            userId = response.getBody();
//        } catch (RestClientResponseException | ResourceAccessException e) {
//            BasicLogger.log(e.getMessage());
//        }
//
//        return userId;
//    }

      public Transfer[] getTransferHistory(int accountId){

        Transfer[] history = null;

        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(API_BASE_URL + accountId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Transfer[].class);

            history = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }

        return history;
      }




    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}
