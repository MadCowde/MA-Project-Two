package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import java.math.BigDecimal;
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

    public void printCurrentBalance(int userId) {
        //Goal of this method is to use accountservice to return the balance with the user_id.
        Account account = acc.getAccount(userId);
        System.out.println("The account balance is: " +
                account.getBalance());
    }

    public void printListOfUsers() {
        User[] users = acc.getAllUsers();
        for (User user : users) {
            System.out.println(user.toString());
        }

    }

    public void printTransferHistory(int currentUserId) {
        //Goal of this function is print the Transfer history to the console
        Transfer[] transfersList = acc.getTransferHistory(currentUserId);
        int i = 1;
        System.out.println("\n\t\t\t\tTransfer History\n#:\tPayer:\t\t\tAmount:\t\tPayee:\t\t\t\tStatus:");
        for (Transfer transactions : transfersList) {
            if (currentUserId == acc.findUser(Integer.toString(transactions.getAccount_from())).getId()) {
                User to = acc.findUser(Integer.toString(transactions.getAccount_to()));
                if (transactions.getTransferAmount().compareTo(new BigDecimal(1000)) < 0) {
                    System.out.printf("%d:\t\tYou(%d)\t\t$%.2f\t\t%s(%d)\t\t%s\n", i, currentUserId,
                            transactions.getTransferAmount(),
                            to.getUsername(), to.getId(),
                            (transactions.getTransfer_status_id() == 2) ? "Approved" : "Rejected");
                } else {
                    System.out.printf("%d:\t\tYou(%d)\t\t$%.2f\t%s(%d)\t\t%s\n", i, currentUserId,
                            transactions.getTransferAmount(),
                            to.getUsername(), to.getId(),
                            (transactions.getTransfer_status_id() == 2) ? "Approved" : "Rejected");
                }
                i++;
            } else {
                User from = acc.findUser(Integer.toString(transactions.getAccount_from()));
                if (transactions.getTransferAmount().compareTo(new BigDecimal(1000)) < 0) {
                    System.out.printf("%d:\t\t%s(%d)\t\t$%.2f\t\tyou(%d)\t\t%s\n", i,
                            from.getUsername(), from.getId(),
                            transactions.getTransferAmount(), currentUserId,
                            (transactions.getTransfer_status_id() == 2) ? "Approved" : "Rejected");
                } else {
                    System.out.printf("%d:\t\t%s(%d)\t\t$%.2f\tyou(%d)\t\t%s\n", i,
                            from.getUsername(), from.getId(),
                            transactions.getTransferAmount(), currentUserId,
                            (transactions.getTransfer_status_id() == 2) ? "Approved" : "Rejected");
                }
                i++;
            }
        }
    }

    public void printPendingRequests(int currentUserId) {
        //Goal of this function is print the pending requests to the console
        Transfer[] pendingList = acc.getPendingRequests(currentUserId);
        //Need to add logic to be able to get transfer status.
        System.out.println("\n\t\t\tTransfers Pending Approval");
        if (pendingList == null) {
            System.out.println("There are no pending approvals.");
            return;
        }
        System.out.println("#:\t\tRecipient:\t\tSender:\t\t\tAmount:\t\tStatus:");
        int i = 1;
        for (Transfer pending : pendingList) {
            System.out.printf("%d:\t\t%s(%d)\t\t%s(%d)\t\t%.2f\t\tPending\n", i,
                    acc.findUser(Integer.toString(pending.getAccount_to())).getUsername(),
                    pending.getAccount_to(), acc.findUser(Integer.toString(pending.getAccount_from())).getUsername(),
                    pending.getAccount_from(), pending.getTransferAmount());
            i++;
        }

    }

    public void sendMoneyRequest(int currentUserId) {
        //Basic functionality : Pull current user ID. Prompt for user to send money to
        System.out.println("\n");
        printListOfUsers();
        int userTo = promptForInt("\nPlease enter the user_id you are sending money to: ");
        int userFrom = currentUserId;
        BigDecimal amount = promptForBigDecimal("How much would you like to send? ");
        acc.sendMoney(userTo, userFrom, amount);

    }

    public void requestMoneyFrom(int currentUserId) {
        System.out.println("\n");
        printListOfUsers();
        int userRequesting = currentUserId;
        int userFrom = promptForInt("Please enter the user_id you are requesting money from: ");
        BigDecimal amount = promptForBigDecimal("How much would you like to request? ");
        acc.requestMoney(userRequesting, userFrom, amount);

    }

}
