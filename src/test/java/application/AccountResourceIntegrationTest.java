package application;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

/**
 * Integration test for {@link AccountResource}, testing end-to-end HTTP requests and responses.
 * Given the resource class itself is so simple we've opted just for integration tests to make sure
 * the app is all wired up and behaving correctly: usually there would be accompanying unit tests as well.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountResourceIntegrationTest {
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
    public void testGetValidIndividualAccountReturnsCorrectJson() throws Exception {
        final Account savingsAccount = ModelDataTestUtil.createSavingsAccount(ACCOUNT_NUMBER);
        when(dataAccessLayer.getAccount(USERNAME, ACCOUNT_NUMBER)).thenReturn(savingsAccount);

        // Check all the returned fields in the json
        mockMvc.perform(get("/account/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("accountNumber", equalTo(savingsAccount.getAccountNumber())))
                .andExpect(jsonPath("accountName", equalTo(savingsAccount.getAccountName())))
                .andExpect(jsonPath("accountType", equalTo(savingsAccount.getAccountType().name())))
                .andExpect(jsonPath("balanceDate", equalTo(savingsAccount.getBalanceDate().toString())))
                .andExpect(jsonPath("currency", equalTo(savingsAccount.getCurrency().getCurrencyCode())))
                .andExpect(jsonPath("balance", equalTo(savingsAccount.getBalance())));
    }

    @Test
    public void testGetNonExistentAccountReturns404() throws Exception {
        mockMvc.perform(get("/account/12345"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testListAccountsReturnsCorrectJson() throws Exception {
        final Account account1 = ModelDataTestUtil.createSavingsAccount(ACCOUNT_NUMBER);
        final Account account2 = ModelDataTestUtil.createSavingsAccount(ACCOUNT_NUMBER+1);
        final Account account3 = ModelDataTestUtil.createSavingsAccount(ACCOUNT_NUMBER+2);
        when(dataAccessLayer.getAccounts(USERNAME)).thenReturn(Lists.list(account1, account2, account3));

        // Ensure we get the 3 accounts correctly returned
        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].accountNumber", equalTo(account1.getAccountNumber())))
                .andExpect(jsonPath("$[1].accountNumber", equalTo(account2.getAccountNumber())))
                .andExpect(jsonPath("$[2].accountNumber", equalTo(account3.getAccountNumber())));
    }
}

