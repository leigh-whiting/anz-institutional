package anz.application;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import anz.model.Account;
import anz.model.Transaction;
import anz.services.DataAccessLayer;

/**
 * Rest API resource for {@link Transaction} objects.
 */
@RestController
public class TransactionResource {

    private final DataAccessLayer dataAccessLayer;

    @Autowired
    public TransactionResource(final DataAccessLayer dataAccessLayer) {
        this.dataAccessLayer = dataAccessLayer;
    }

    /**
     * Fetches all transactions in the provided account for the logged in user
     */
    @PostMapping("/transaction/list")
    public List<Transaction> transactionList(@RequestBody final TransactionRequest request, final Pageable pageable) {
        final Account account = dataAccessLayer.getAccount(request.getAccountId());
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found");
        }
        return dataAccessLayer.getTransactions(account.getId(), pageable);
    }

    /**
     * Simple, extendable bean that holds search parameters for listing transactions
     */
    public static class TransactionRequest {
        private UUID accountId;

        UUID getAccountId() {
            return accountId;
        }

        public void setAccountId(final UUID accountId) {
            this.accountId = accountId;
        }
    }
}
