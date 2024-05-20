package co.istad.easybanking.api.transaction;

import co.istad.easybanking.api.account.Account;
import co.istad.easybanking.api.staff.Staff;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction")
@ToString
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;

    private String currency;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "debit_account_id")
    private Account debitAccount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_account_id")
    private Account creditAccount;

    private BigDecimal amount;

    private String transactionType;

    private LocalDateTime transferDate;

    private String transactionDesc;

    private Boolean authorize;

    private String sender;

    @ManyToOne
    private Staff staffId;

    @ManyToOne
    private Consumer consumer;
}