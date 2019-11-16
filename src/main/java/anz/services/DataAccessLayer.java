package anz.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import anz.model.Account;
import anz.model.Transaction;

/**
 * Abstraction interface representing service that allows access to data in persistence.
 */
public interface DataAccessLayer {

    /**
     * Gets the account with the provided account number for the given user
     */
    Account getAccount(final UUID accountId);

    /**
     * Fetches all accounts for the given user.
     */
    List<Account> getAccounts(final String username, final Pageable pageable);

    /**
     * Fetches all transactions for the provided account and user
     */
    List<Transaction> getTransactions(final UUID accountId, final Pageable pageable);
}
