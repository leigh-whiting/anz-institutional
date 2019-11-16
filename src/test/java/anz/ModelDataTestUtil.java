package anz;

import static java.lang.Math.abs;

import static anz.model.Transaction.TransactionType.CREDIT;
import static anz.model.Transaction.TransactionType.DEBIT;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Currency;
import java.util.UUID;

import anz.model.Account;
import anz.model.Transaction;

/**
 * Utility methods for creating {@link Account} and {@link Transaction} objects
 */
public final class ModelDataTestUtil {
    private static final Currency AUD = Currency.getInstance("AUD");

    private static Instant utcInstant(final int year, final int month, final int day) {
        return LocalDateTime.of(year, month, day, 0, 0).toInstant(ZoneOffset.UTC);
    }

    public static Account createSavingsAccount(final int accountNumber) {
        return new Account()
                .setId(UUID.randomUUID())
                .setAccountNumber(accountNumber)
                .setAccountName("Savings account")
                .setAccountType(Account.AccountType.SAVINGS)
                .setBalance(1234.56)
                .setCurrency(AUD)
                .setBalanceDate(utcInstant(2019, 10, 11));
    }

    public static Transaction createTransaction(final Account account, final double amount) {
        return new Transaction()
                .setId(UUID.randomUUID())
                .setAccount(account)
                .setValueDate(utcInstant(2019, 10, 11))
                .setCurrency(AUD)
                .setAmount(abs(amount))
                .setTransactionType(amount > 0 ? CREDIT : DEBIT)
                .setTransactionNarrative("something");
    }
}
