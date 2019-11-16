package anz.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import anz.model.Account;

/**
 * JPA repository for {@link Account} objects
 */
public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findByUsername(final String username, final Pageable pageable);
}
