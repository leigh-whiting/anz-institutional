package anz.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import static anz.ModelDataTestUtil.createSavingsAccount;
import static anz.ModelDataTestUtil.createTransaction;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import anz.model.Account;
import anz.model.Transaction;
import anz.repository.AccountRepository;
import anz.repository.TransactionRepository;

/**
 * Simple tests to make sure the JPA repositories are working as expected
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
class DataAccessServiceIntegrationTest {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    private DataAccessLayer dataAccess;

    @BeforeEach
    void init() {
        dataAccess = new DataAccessService(accountRepository, transactionRepository);
    }

    @Test
    void testSaveAndLoadAccount() {
        final Account account = accountRepository.save(createSavingsAccount(12345));
        final Account loadedAccount = dataAccess.getAccount(account.getId());
        assertThat(loadedAccount.getId(), is(account.getId()));
        assertThat(loadedAccount.getAccountName(), is(account.getAccountName()));
    }

    @Test
    void testLoadTransactionsViaAccount() {
        // Load transactions
        final Account account = accountRepository.save(createSavingsAccount(1533));
        transactionRepository.saveAll(Lists.list(
                createTransaction(account, 23.6),
                createTransaction(account, 28.1),
                createTransaction(account, -13.3)));

        // Ensure they can be read
        List<Transaction> loadedTransactions = dataAccess.getTransactions(account.getId(), Pageable.unpaged());
        assertThat(loadedTransactions, hasSize(3));
        assertThat(loadedTransactions.stream()
                .mapToDouble(t -> t.getTransactionType() == Transaction.TransactionType.CREDIT ? t.getAmount() : -t.getAmount())
                .sum(),
                closeTo(38.4, 1E-6));

        // Ensure they can be paged
        loadedTransactions = dataAccess.getTransactions(account.getId(), PageRequest.of(0, 1));
        assertThat(loadedTransactions, hasSize(1));
    }
}
