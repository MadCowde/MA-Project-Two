package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.TransferType;
import com.techelevator.util.BasicLogger;
import io.cucumber.core.resource.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class TransferService {

    public static final String API_BASE_URL = "http://localhost:8080/transfers/";

    private RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


    public Transfer postTransferRequest(int transferType,
                                         int transferTo, int transferFrom, BigDecimal amountToTransfer){

        Transfer newTransfer = new Transfer(transferType,
                transferTo, transferFrom, amountToTransfer);


        HttpEntity<Transfer> entity = makeEntity(newTransfer);

        Transfer transferPost = null;
        try {
            transferPost = restTemplate.postForObject(API_BASE_URL, entity, Transfer.class);
        } catch (RestClientResponseException ex){
            BasicLogger.log(ex.getRawStatusCode() + " : " + ex.getStatusText());
        } catch (ResourceAccessException ex){
            BasicLogger.log(ex.getMessage());
        }

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


    private HttpEntity<Transfer> makeEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(transfer, headers);
    }


}
