package model;

import java.time.Instant;
import java.util.Currency;

/**
 * Bean representing a transaction in an account. Some similar assumptions to {@link Account}
 * for simplicity.
 */
public class Transaction {
    private int accountNumber;
    private Instant valueDate;
    private Currency currency;
    private double amount;
    private TransactionType transactionType;
    private String transactionNarrative;

    public enum TransactionType {
        DEBIT,
        CREDIT
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public Transaction setAccountNumber(final int accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public Instant getValueDate() {
        return valueDate;
    }

    public Transaction setValueDate(final Instant valueDate) {
        this.valueDate = valueDate;
        return this;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Transaction setCurrency(final Currency currency) {
        this.currency = currency;
        return this;
    }

    public double getAmount() {
        return amount;
    }

    public Transaction setAmount(final double amount) {
        this.amount = amount;
        return this;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public Transaction setTransactionType(final TransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public String getTransactionNarrative() {
        return transactionNarrative;
    }

    public Transaction setTransactionNarrative(final String transactionNarrative) {
        this.transactionNarrative = transactionNarrative;
        return this;
    }
}
