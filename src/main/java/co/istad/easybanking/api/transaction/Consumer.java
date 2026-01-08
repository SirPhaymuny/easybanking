package co.istad.easybanking.api.transaction;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Consumer")
@ToString
public class Consumer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String billId;

    Long consumerId;

    String consumerName;

    BigDecimal amount;

    @OneToMany(mappedBy = "consumer", cascade = CascadeType.ALL)
    List<Transaction> transactionsId;
}
