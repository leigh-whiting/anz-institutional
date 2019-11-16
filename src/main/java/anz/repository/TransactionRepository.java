package anz.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import anz.model.Transaction;

/**
 * JPA repository for {@link Transaction} objects
 */
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByAccount_Id(final UUID accountId, final Pageable pageable);
}
