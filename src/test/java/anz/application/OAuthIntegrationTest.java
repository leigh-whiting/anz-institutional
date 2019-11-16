package anz.application;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

/**
 * Test that we have wired in OAuth2 correctly
 * This test does not use the "test" profile as it uses Spring Security
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class OAuthIntegrationTest {

    @Autowired
    private AuthorizationServerTokenServices tokenServices;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testOAuthSecurity() throws Exception {
        // A request with no token should be unauthorised
        mockMvc.perform(post("/account/list")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isUnauthorized());

        // A request with a valid token should be authorised
        mockMvc.perform(post("/account/list")
                .with(addOAuthToken("alice"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());
    }

    /**
     * Builds a {@link RequestPostProcessor} that adds an OAuth2 token to the request for the provided username
     */
    private RequestPostProcessor addOAuthToken(final String username) {
        return mockRequest -> {
            // Build a token and set it as the authorisation header
            final OAuth2Request oauth2Request = new OAuth2Request(null, "anz-example", null, true, null, null, null, null, null);
            final Authentication authentication = new TestingAuthenticationToken(username, null, "USER");
            final OAuth2Authentication oauth2auth = new OAuth2Authentication(oauth2Request, authentication);
            final OAuth2AccessToken token = tokenServices.createAccessToken(oauth2auth);

            // Set Authorization header to use Bearer
            mockRequest.addHeader("Authorization", "Bearer " + token.getValue());
            return mockRequest;
        };
    }
}
