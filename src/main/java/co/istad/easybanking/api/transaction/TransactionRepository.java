package co.istad.easybanking.api.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findTransactionByTransactionId(String transactionId);
}
