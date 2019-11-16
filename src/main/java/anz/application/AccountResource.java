package anz.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import anz.model.Account;
import anz.services.AuthenticationProvider;
import anz.services.DataAccessLayer;

/**
 * Rest API resource for {@link Account} objects.
 */
@RestController
public class AccountResource {

    private final AuthenticationProvider authenticationProvider;
    private final DataAccessLayer dataAccessLayer;

    @Autowired
    public AccountResource(
            final AuthenticationProvider authenticationProvider,
            final DataAccessLayer dataAccessLayer) {
        this.authenticationProvider = authenticationProvider;
        this.dataAccessLayer = dataAccessLayer;
    }

    /**
     * Fetches all accounts for the logged in user
     */
    @PostMapping("/account/list")
    public List<Account> accountList(@RequestBody final AccountRequest accountRequest, final Pageable pageable) {
        return dataAccessLayer.getAccounts(authenticationProvider.getAuthenticatedUser().getUsername(), pageable);
    }

    /**
     * Simple, extendable bean that holds search parameters for listing accounts
     * Empty at this point in the exercise, but easily extensible
     */
    static class AccountRequest {

    }
}
