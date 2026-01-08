package co.istad.easybanking.api.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerRepository extends JpaRepository<Consumer,Long> {
    Consumer findConsumerByBillId(String billId);
    Consumer findConsumerByConsumerId(Long consumerId);
}
