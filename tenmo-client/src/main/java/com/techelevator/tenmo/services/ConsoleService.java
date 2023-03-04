package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.security.Principal;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {
    private AccountService acc = new AccountService();
    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

    public void printCurrentBalance(int userId){
      //Goal of this method is to use accountservice to return the balance with the user_id.
        Account account = acc.getAccount(userId);
        System.out.println("The account balance is : " +
        account.getBalance());
    }

    public void printTransferHistory(int currentUserId){
        //Goal of this function is print the Transfer history to the console
      Transfer[] transfersList = acc.getTransferHistory(currentUserId);
        for (Transfer transactions : transfersList){
            System.out.println(transactions.getAccount_from() + " has paid " + transactions.getAccount_to()
                    + "\n $:" + transactions.getTransferAmount());

            }

        }

        public void printPendingRequests(int currentUserId){
        //Goal of this function is print the pending requests to the console
            Transfer[] pendingList = acc.getPendingRequests(currentUserId);
            //Need to add logic to be able to get transfer status.
            for (Transfer pending : pendingList){
                System.out.println("The transfer to " + pending.getAccount_to() + " from " +
                        pending.getAccount_from() + " is Pending");
            }

        }

        public void sendMoneyRequest(int currentUserId){
         //Basic functionality : Pull current user ID. Prompt for user to send money to
            int userTo = promptForInt("Please enter the user_id you are sending money to");
            int userFrom = currentUserId;
            BigDecimal amount = promptForBigDecimal("How much would you like to send?");
            acc.sendMoney(userTo, userFrom, amount);

        }

        public void requestMoneyFrom(int currentUserId){
        int userRequesting = currentUserId;
        int userFrom = promptForInt("Please enter the user_id you are requesting money from");
        BigDecimal amount = promptForBigDecimal("How much would you like to request?");

        acc.requestMoney(userRequesting, userFrom, amount);

        }

    }



