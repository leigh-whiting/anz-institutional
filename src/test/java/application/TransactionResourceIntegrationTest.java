package application;

import static java.lang.Math.abs;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static application.ModelDataTestUtil.createSavingsAccount;
import static application.ModelDataTestUtil.createTransaction;
import static model.Transaction.TransactionType.CREDIT;
import static model.Transaction.TransactionType.DEBIT;

import java.security.Principal;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import model.Account;
import model.Transaction;

/**
 * Integration test for {@link TransactionResource}, testing end-to-end HTTP requests and responses.
 * Given the resource class itself is so simple we've opted just for integration tests to make sure
 * the app is all wired up and behaving correctly: usually there would be accompanying unit tests as well.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionResourceIntegrationTest {
    private static final String USERNAME = "user";
    private static final int ACCOUNT_NUMBER = 12345;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @MockBean
    private DataAccessLayer dataAccessLayer;

    @Before
    public void init() {
        // Always ensure a logged in user
        final Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(USERNAME);
        when(authenticationProvider.getAuthenticatedUser()).thenReturn(principal);
    }

    @Test
    public void testGetTransactionsForNonExistentAccountReturns404() throws Exception {
        mockMvc.perform(get("/transaction/12345"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetTransactionsForValidAccountReturnsCorrectJson() throws Exception {
        final Account account = createSavingsAccount(ACCOUNT_NUMBER);
        when(dataAccessLayer.getAccount(USERNAME, ACCOUNT_NUMBER)).thenReturn(account);

        final Transaction transaction1 = createTransaction(ACCOUNT_NUMBER, 12.34);
        final Transaction transaction2 = createTransaction(ACCOUNT_NUMBER, -34.56);
        final Transaction transaction3 = createTransaction(ACCOUNT_NUMBER, 67.89);
        when(dataAccessLayer.getTransactions(USERNAME, ACCOUNT_NUMBER)).thenReturn(Lists.list(transaction1, transaction2, transaction3));

        // Check the returned fields in the json
        mockMvc.perform(get("/transaction/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].accountNumber", equalTo(ACCOUNT_NUMBER)))
                .andExpect(jsonPath("$[0].valueDate", equalTo(transaction1.getValueDate().toString())))
                .andExpect(jsonPath("$[0].currency", equalTo(transaction1.getCurrency().getCurrencyCode())))
                .andExpect(jsonPath("$[0].amount", equalTo(transaction1.getAmount())))
                .andExpect(jsonPath("$[0].transactionType", equalTo(CREDIT.name())))
                .andExpect(jsonPath("$[0].transactionNarrative", equalTo(transaction1.getTransactionNarrative())))

                .andExpect(jsonPath("$[1].accountNumber", equalTo(ACCOUNT_NUMBER)))
                .andExpect(jsonPath("$[1].valueDate", equalTo(transaction2.getValueDate().toString())))
                .andExpect(jsonPath("$[1].currency", equalTo(transaction2.getCurrency().getCurrencyCode())))
                .andExpect(jsonPath("$[1].amount", equalTo(abs(transaction2.getAmount()))))
                .andExpect(jsonPath("$[1].transactionType", equalTo(DEBIT.name())))
                .andExpect(jsonPath("$[1].transactionNarrative", equalTo(transaction2.getTransactionNarrative())))

                .andExpect(jsonPath("$[2].accountNumber", equalTo(ACCOUNT_NUMBER)))
                .andExpect(jsonPath("$[2].valueDate", equalTo(transaction3.getValueDate().toString())))
                .andExpect(jsonPath("$[2].currency", equalTo(transaction3.getCurrency().getCurrencyCode())))
                .andExpect(jsonPath("$[2].amount", equalTo(transaction3.getAmount())))
                .andExpect(jsonPath("$[2].transactionType", equalTo(CREDIT.name())))
                .andExpect(jsonPath("$[2].transactionNarrative", equalTo(transaction2.getTransactionNarrative())));
    }
}

