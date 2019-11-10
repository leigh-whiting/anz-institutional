package application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import model.Account;
import model.Transaction;

/**
 * Rest API resource for {@link Transaction} objects.
 */
@RestController
public class TransactionResource {

    private final AuthenticationProvider authenticationProvider;
    private final DataAccessLayer dataAccessLayer;

    @Autowired
    public TransactionResource(
            final AuthenticationProvider authenticationProvider,
            final DataAccessLayer dataAccessLayer) {
        this.authenticationProvider = authenticationProvider;
        this.dataAccessLayer = dataAccessLayer;
    }

    /**
     * Fetches all transactions in the provided account for the logged in user
     */
    @GetMapping("/transaction/{accountNumber}")
    public List<Transaction> transactionList(@PathVariable final int accountNumber) {
        final String username = authenticationProvider.getAuthenticatedUser().getName();
        final Account account = dataAccessLayer.getAccount(username, accountNumber);
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found");
        }
        return dataAccessLayer.getTransactions(username, accountNumber);
    }
}
