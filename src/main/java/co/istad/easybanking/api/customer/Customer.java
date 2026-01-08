package co.istad.easybanking.api.customer;

import co.istad.easybanking.api.specimen.CustomerSpecimen;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import co.istad.easybanking.api.account.Account;

@Builder
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long Id;

    @Column(name = "Customer_Id")
    private Long customerId;

    @Column(name = "Customer_firstName")
    private String firstName;

    @Column(name = "Customer_lastName")
    private String lastName;

    @Column(name = "FullName")
    private String fullName;

    @Column(name = "Date_of_Birth")
    private LocalDate dateOfBirth;

    @Column(name = "National_Identity")
    private String nid;

    @Column(name = "Gender")
    private String gender;

    @Column(name = "Phone_Number")
    private String phoneNumber;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Account> accounts;

    @OneToMany(mappedBy = "customerId", cascade = CascadeType.ALL)
    private List<CustomerSpecimen> imageId;
}
