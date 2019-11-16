package anz.model;

import java.time.Instant;
import java.util.Currency;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Entity representing an account
 *
 * Plenty of assumptions here for simplicity:
 *  - Instants for dates? Assuming a server stores UTC timestamps - the UI can translate
 *  - Balance as a double? Would be some sort of fixed point decimal to correctly store fractional cents I imagine
 */
@Entity
public final class Account {

    @Id
    @GeneratedValue
    @Column
    private UUID id;
    @Column
    private int accountNumber;
    @Column
    private String accountName;
    @Column
    private String username;
    @Column
    private AccountType accountType;
    @Column
    private Instant balanceDate;
    @Column
    private Currency currency;
    @Column
    private double balance;

    public enum AccountType {
        SAVINGS,
        CURRENT
    }

    public UUID getId() {
        return id;
    }

    public Account setId(final UUID id) {
        this.id = id;
        return this;
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

    public String getUsername() {
        return username;
    }

    public Account setUsername(final String username) {
        this.username = username;
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
