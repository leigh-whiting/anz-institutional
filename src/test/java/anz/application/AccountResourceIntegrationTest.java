package anz.application;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import anz.ModelDataTestUtil;
import anz.model.Account;
import anz.services.AuthenticationProvider;
import anz.services.DataAccessLayer;

/**
 * Integration test for {@link AccountResource}, testing end-to-end HTTP requests and responses.
 * Given the resource class itself is so simple we've opted just for integration tests to make sure
 * the app is all wired up and behaving correctly: usually there would be accompanying unit tests as well.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AccountResourceIntegrationTest {
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
    void testListAccountsReturnsCorrectJson() throws Exception {
        final Account account1 = ModelDataTestUtil.createSavingsAccount(ACCOUNT_NUMBER);
        final Account account2 = ModelDataTestUtil.createSavingsAccount(ACCOUNT_NUMBER+1);
        final Account account3 = ModelDataTestUtil.createSavingsAccount(ACCOUNT_NUMBER+2);
        when(dataAccessLayer.getAccounts(eq(USERNAME), any())).thenReturn(Lists.list(account1, account2, account3));

        // Ensure we get the 3 accounts correctly returned
        mockMvc.perform(post("/account/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].accountNumber", equalTo(account1.getAccountNumber())))
                .andExpect(jsonPath("$[1].accountNumber", equalTo(account2.getAccountNumber())))
                .andExpect(jsonPath("$[2].accountNumber", equalTo(account3.getAccountNumber())));
    }
}

