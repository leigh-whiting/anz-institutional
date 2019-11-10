package application;

import static java.lang.Math.abs;

import static model.Transaction.TransactionType.CREDIT;
import static model.Transaction.TransactionType.DEBIT;
import static util.DateTimeUtil.utcInstant;

import java.util.Currency;

import model.Account;
import model.Transaction;

/**
 * Utility methods for creating {@link Account} and {@link Transaction} objects
 */
final class ModelDataTestUtil {
    private static final Currency AUD = Currency.getInstance("AUD");

    static Account createSavingsAccount(final int accountNumber) {
        return new Account()
                .setAccountNumber(accountNumber)
                .setAccountName("Savings account")
                .setAccountType(Account.AccountType.SAVINGS)
                .setBalance(1234.56)
                .setCurrency(AUD)
                .setBalanceDate(utcInstant(2019, 10, 11));
    }

    static Transaction createTransaction(final int accountNumber, final double amount) {
        return new Transaction()
                .setAccountNumber(accountNumber)
                .setValueDate(utcInstant(2019, 10, 11))
                .setCurrency(AUD)
                .setAmount(abs(amount))
                .setTransactionType(amount > 0 ? CREDIT : DEBIT)
                .setTransactionNarrative("something");
    }
}
