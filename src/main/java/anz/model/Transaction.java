package anz.model;

import java.time.Instant;
import java.util.Currency;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Entity representing a transaction in an account. Some similar assumptions to {@link Account}
 * for simplicity.
 */
@Entity
public class Transaction {

    @Id
    @GeneratedValue
    @Column
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "account_Id")
    private Account account;
    @Column
    private Instant valueDate;
    @Column
    private Currency currency;
    @Column
    private double amount;
    @Column
    private TransactionType transactionType;
    @Column
    private String transactionNarrative;

    public enum TransactionType {
        DEBIT,
        CREDIT
    }

    public UUID getId() {
        return id;
    }

    public Transaction setId(final UUID id) {
        this.id = id;
        return this;
    }

    public Account getAccount() {
        return account;
    }

    public Transaction setAccount(final Account account) {
        this.account = account;
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
