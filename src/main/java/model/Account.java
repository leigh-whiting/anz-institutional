package model;

import java.time.Instant;
import java.util.Currency;

/**
 * Bean representing an account
 *
 * Plenty of assumptions here for simplicity:
 *  - Integer account number? Probably would be a strongly typed id
 *  - Instants for dates? Assuming a server stores UTC timestamps - the UI can translate
 *  - Balance as a double? Would be some sort of fixed point decimal to correctly store fractional cents I imagine
 */
public final class Account {
    private int accountNumber;
    private String accountName;
    private AccountType accountType;
    private Instant balanceDate;
    private Currency currency;
    private double balance;

    public enum AccountType {
        SAVINGS,
        CURRENT
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public Account setAccountNumber(final int accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public String getAccountName() {
        return accountName;
    }

    public Account setAccountName(final String accountName) {
        this.accountName = accountName;
        return this;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public Account setAccountType(final AccountType accountType) {
        this.accountType = accountType;
        return this;
    }

    public Instant getBalanceDate() {
        return balanceDate;
    }

    public Account setBalanceDate(final Instant balanceDate) {
        this.balanceDate = balanceDate;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Account setCurrency(final Currency currency) {
        this.currency = currency;
        return this;
    }

    public double getBalance() {
        return balance;
    }

    public Account setBalance(final double balance) {
        this.balance = balance;
        return this;
    }
}
