package application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import model.Account;

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
     * Fetches the requested account for the logged in user
     */
    @GetMapping("/account/{accountNumber}")
    public Account account(@PathVariable final int accountNumber) {
        final Account account = dataAccessLayer.getAccount(authenticationProvider.getAuthenticatedUser().getName(), accountNumber);
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        return account;
    }

    /**
     * Fetches all accounts for the logged in user
     */
    @GetMapping("/account")
    public List<Account> accountList() {
        return dataAccessLayer.getAccounts(authenticationProvider.getAuthenticatedUser().getName());
    }
}
