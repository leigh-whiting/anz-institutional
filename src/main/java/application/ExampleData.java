package application;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import static model.Transaction.TransactionType.CREDIT;
import static util.DateTimeUtil.utcInstant;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import model.Account;
import model.Transaction;

/**
 * Stub beans used to allow launch via main
 */
@Configuration
public class ExampleData {
    private static final String USERNAME = "user foo";
    private static final Currency AUD = Currency.getInstance("AUD");

    private static final Account ACCOUNT_1 = new Account()
            .setAccountNumber(1002435693)
            .setAccountName("Savings account")
            .setAccountType(Account.AccountType.SAVINGS)
            .setBalance(1234.56)
            .setCurrency(AUD)
            .setBalanceDate(utcInstant(2019, 10, 11));

    private static final Account ACCOUNT_2 = new Account()
            .setAccountNumber(1002544393)
            .setAccountName("Current account")
            .setAccountType(Account.AccountType.CURRENT)
            .setBalance(1245.36)
            .setCurrency(AUD)
            .setBalanceDate(utcInstant(2019, 10, 11));

    private static final Transaction TRANSACTION_1 = new Transaction()
            .setAccountNumber(ACCOUNT_1.getAccountNumber())
            .setValueDate(utcInstant(2019, 10, 11))
            .setCurrency(AUD)
            .setAmount(ACCOUNT_1.getBalance())
            .setTransactionType(CREDIT)
             .setTransactionNarrative("opening deposit");

    private static final Transaction TRANSACTION_2 = new Transaction()
            .setAccountNumber(ACCOUNT_2.getAccountNumber())
            .setValueDate(utcInstant(2019, 10, 11))
            .setCurrency(AUD)
            .setAmount(ACCOUNT_2.getBalance())
            .setTransactionType(CREDIT)
            .setTransactionNarrative("opening deposit");

    private static final List<Account> ACCOUNT_LIST = new ArrayList<>();
    static {
        ACCOUNT_LIST.add(ACCOUNT_1);
        ACCOUNT_LIST.add(ACCOUNT_2);
    }

    @Bean
    AuthenticationProvider mockAuthenticationProvider() {
        return () -> () -> USERNAME;
    }

    @Bean
    DataAccessLayer mockDataAccessLayer() {
        return new DataAccessLayer() {
            @Override
            public Account getAccount(final String username, final int accountNumber) {
                return USERNAME.equals(username)
                        ? (ACCOUNT_1.getAccountNumber() == accountNumber ? ACCOUNT_1
                            : (ACCOUNT_2.getAccountNumber() == accountNumber ? ACCOUNT_2 : null))
                            : null;
            }

            @Override
            public List<Account> getAccounts(final String username) {
                return USERNAME.equals(username) ? ACCOUNT_LIST : emptyList();
            }

            @Override
            public List<Transaction> getTransactions(final String username, final int accountNumber) {
                return USERNAME.equals(username)
                        ? (ACCOUNT_1.getAccountNumber() == accountNumber ? singletonList(TRANSACTION_1)
                        : (ACCOUNT_2.getAccountNumber() == accountNumber ? singletonList(TRANSACTION_2) : null))
                        : null;
            }
        };
    }
}
