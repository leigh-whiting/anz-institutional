package anz.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import anz.model.Account;
import anz.model.Transaction;
import anz.repository.AccountRepository;
import anz.repository.TransactionRepository;

/**
 * Implementation of {@link DataAccessLayer} using JPA repositories
 */
@Service
public class DataAccessService implements DataAccessLayer {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public DataAccessService(final AccountRepository accountRepository, final TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Account getAccount(final UUID accountId) {
        return accountRepository.getOne(accountId);
    }

    @Override
    public List<Account> getAccounts(final String username, final Pageable pageable) {
        return accountRepository.findByUsername(username, pageable);
    }

    @Override
    public List<Transaction> getTransactions(final UUID accountId, final Pageable pageable) {
        return transactionRepository.findByAccount_Id(accountId, pageable);
    }
}
