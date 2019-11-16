package anz.application;

import static java.lang.Math.abs;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static anz.ModelDataTestUtil.createSavingsAccount;
import static anz.ModelDataTestUtil.createTransaction;
import static anz.model.Transaction.TransactionType.CREDIT;
import static anz.model.Transaction.TransactionType.DEBIT;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import anz.model.Account;
import anz.model.Transaction;
import anz.services.AuthenticationProvider;
import anz.services.DataAccessLayer;

/**
 * Integration test for {@link TransactionResource}, testing end-to-end HTTP requests and responses.
 * Given the resource class itself is so simple we've opted just for integration tests to make sure
 * the app is all wired up and behaving correctly: usually there would be accompanying unit tests as well.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TransactionResourceIntegrationTest {
    private static final String USERNAME = "user";
    private static final int ACCOUNT_NUMBER = 12345;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationProvider authenticationProvider;
    @MockBean
    private DataAccessLayer dataAccessLayer;

    @BeforeEach
    void init() {
        // Always ensure a logged in user
        final UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(USERNAME);
        when(authenticationProvider.getAuthenticatedUser()).thenReturn(userDetails);
    }

    @Test
    void testListTransactionsForNonExistentAccountReturns404() throws Exception {
        mockMvc.perform(post("/transaction/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountId\": \"bfb74738-2726-423b-b492-d656cc01c4e9\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testListTransactionsForValidAccountReturnsCorrectJson() throws Exception {
        final Account account = createSavingsAccount(ACCOUNT_NUMBER);
        when(dataAccessLayer.getAccount(account.getId())).thenReturn(account);

        final Transaction transaction1 = createTransaction(account, 12.34);
        final Transaction transaction2 = createTransaction(account, -34.56);
        final Transaction transaction3 = createTransaction(account, 67.89);
        when(dataAccessLayer.getTransactions(eq(account.getId()), any())).thenReturn(Lists.list(transaction1, transaction2, transaction3));

        // Check the returned fields in the json
        mockMvc.perform(post("/transaction/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountId\": \"" + account.getId() + "\"}"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(3)))

                .andExpect(jsonPath("$[0].account.accountNumber", equalTo(ACCOUNT_NUMBER)))
                .andExpect(jsonPath("$[0].valueDate", equalTo(transaction1.getValueDate().toString())))
                .andExpect(jsonPath("$[0].currency", equalTo(transaction1.getCurrency().getCurrencyCode())))
                .andExpect(jsonPath("$[0].amount", equalTo(transaction1.getAmount())))
                .andExpect(jsonPath("$[0].transactionType", equalTo(CREDIT.name())))
                .andExpect(jsonPath("$[0].transactionNarrative", equalTo(transaction1.getTransactionNarrative())))

                .andExpect(jsonPath("$[1].account.accountNumber", equalTo(ACCOUNT_NUMBER)))
                .andExpect(jsonPath("$[1].valueDate", equalTo(transaction2.getValueDate().toString())))
                .andExpect(jsonPath("$[1].currency", equalTo(transaction2.getCurrency().getCurrencyCode())))
                .andExpect(jsonPath("$[1].amount", equalTo(abs(transaction2.getAmount()))))
                .andExpect(jsonPath("$[1].transactionType", equalTo(DEBIT.name())))
                .andExpect(jsonPath("$[1].transactionNarrative", equalTo(transaction2.getTransactionNarrative())))

                .andExpect(jsonPath("$[2].account.accountNumber", equalTo(ACCOUNT_NUMBER)))
                .andExpect(jsonPath("$[2].valueDate", equalTo(transaction3.getValueDate().toString())))
                .andExpect(jsonPath("$[2].currency", equalTo(transaction3.getCurrency().getCurrencyCode())))
                .andExpect(jsonPath("$[2].amount", equalTo(transaction3.getAmount())))
                .andExpect(jsonPath("$[2].transactionType", equalTo(CREDIT.name())))
                .andExpect(jsonPath("$[2].transactionNarrative", equalTo(transaction2.getTransactionNarrative())));
    }

    @Test
    void testListTransactionsWithPagination() throws Exception {
        final Account account = createSavingsAccount(ACCOUNT_NUMBER);
        when(dataAccessLayer.getAccount(account.getId())).thenReturn(account);

        mockMvc.perform(post("/transaction/list?page=2&size=10")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountId\": \"" + account.getId() + "\"}"))
                .andExpect(status().isOk());

        verify(dataAccessLayer).getTransactions(eq(account.getId()), eq(PageRequest.of(2,10)));
    }
}

