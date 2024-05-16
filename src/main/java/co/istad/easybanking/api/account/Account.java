package co.istad.easybanking.api.account;

import java.math.BigDecimal;
import java.util.Random;

import co.istad.easybanking.api.customer.Customer;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Account")
public class Account {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long Id;

        @Column(name = "Account_number")
        private Long accountNumber;

        @Column(name = "Account_name")
        private String accountName;

        @ManyToOne
        @JoinColumn(name = "category_id")
        private Category accountType;

        @Column(name = "Account_currency")
        private String currency;

        @Column(name = "balance")
        private BigDecimal balance;

        @Column(name = "Account_Status")
        private Boolean accountStatus;

        @ManyToOne()
        @JoinColumn(name = "Customer_id")
        @JoinTable(name = "Account_Customer", joinColumns = @JoinColumn(name = "Account_number"), inverseJoinColumns = @JoinColumn(name = "Customer_id"))
        private Customer customer;
}
