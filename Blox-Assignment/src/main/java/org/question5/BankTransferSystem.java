package org.question5;

import java.util.UUID;

public class BankTransferSystem {
    private static class Bank {
        private final String name;
        private int balance;

        public Bank(String name, int balance) {
            this.name = name;
            this.balance = balance;
        }

        public synchronized boolean debit(int amount) {
            if (balance >= amount) {
                balance -= amount;
                return true;
            }
            return false;
        }

        public synchronized void credit(int amount) {
            balance += amount;
        }

        public int getBalance() {
            return balance;
        }

        @Override
        public String toString() {
            return "Bank{" + "name='" + name + '\'' + ", balance=" + balance + '}';
        }
    }

    // Transaction logic
    public static void transferMoney(Bank sender, Bank receiver, int amount) {
        String transactionId = UUID.randomUUID().toString(); // Unique transaction ID
        try {
            System.out.println("Starting transaction: " + transactionId);

            // Step 1: Debit sender's account
            if (!sender.debit(amount)) {
                throw new Exception("Insufficient balance in sender's account");
            }
            System.out.println("Debited " + amount + " from " + sender.name);

            // Simulate network call (with possible failure)
            simulateNetworkDelay();

            // Step 2: Credit receiver's account
            receiver.credit(amount);
            System.out.println("Credited " + amount + " to " + receiver.name);

            // Step 3: Log success
            System.out.println("Transaction " + transactionId + " completed successfully");
        } catch (Exception e) {
            // Step 4: Handle failure
            System.err.println("Transaction " + transactionId + " failed: " + e.getMessage());
            System.out.println("Rolling back...");

            // Compensating transaction: Refund sender
            sender.credit(amount);
        }
    }

    // Simulate network delay or failure
    private static void simulateNetworkDelay() throws Exception {
        if (Math.random() < 0.2) { // 20% chance of failure
            throw new Exception("Network error");
        }
    }

    public static void main(String[] args) {
        Bank bankA = new Bank("Bank A", 1000);
        Bank bankB = new Bank("Bank B", 500);

        System.out.println("Initial Balances:");
        System.out.println(bankA);
        System.out.println(bankB);

        // Perform a transaction
        transferMoney(bankA, bankB, 200);

        System.out.println("Final Balances:");
        System.out.println(bankA);
        System.out.println(bankB);
    }
}