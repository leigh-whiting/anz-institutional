package application;

import java.util.List;

import model.Account;
import model.Transaction;

/**
 * Abstraction interface representing *something* that allows access to data in persistence
 * Seems beyond the necessary scope of the exercise to wire in a database (even h2 or something else in-memory)
 */
public interface DataAccessLayer {

    /**
     * Gets the account with the provided account number for the given user
     */
    Account getAccount(final String username, final int accountNumber);

    /**
     * Fetches all accounts for the given user.
     */
    List<Account> getAccounts(final String username);

    /**
     * Fetches all transactions for the provided account and user
     */
    List<Transaction> getTransactions(final String username, final int accountNumber);
}
