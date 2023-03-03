package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {

    public static final String API_BASE_URL = "http://localhost:8080/";

    private RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


    public Transfer postTransferRequest(int transferType, int transferStatus,
                                         int transferTo, int transferFrom, BigDecimal amountToTransfer){

        Transfer newTransfer = new Transfer(transferType, transferStatus,
                transferTo, transferFrom, amountToTransfer);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Transfer> entity = new HttpEntity<>(newTransfer, headers);

        Transfer transferPost = restTemplate.postForObject(API_BASE_URL, entity, Transfer.class);

        return transferPost;


    }

    public TransferStatus postTransferStatus(String status){
        TransferStatus newStatus = new TransferStatus(status);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TransferStatus> entity = new HttpEntity<>(newStatus, headers);

        TransferStatus postStatus = restTemplate.postForObject(API_BASE_URL, entity, TransferStatus.class);

        return postStatus;
    }

    public TransferType postTransferType(String type){
        TransferType newType = new TransferType(type);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TransferType> entity = new HttpEntity<>(newType, headers);

        TransferType postType = restTemplate.postForObject(API_BASE_URL, entity, TransferType.class);

        return postType;

    }





}
