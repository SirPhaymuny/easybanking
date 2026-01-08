package co.istad.easybanking.api.account;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import co.istad.easybanking.api.account.Account;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Category")
@ToString
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Category_id")
    private Long categoryId;

    @Column(name = "Category_name")
    private String categoryName;

    @OneToMany(mappedBy = "accountType", cascade = CascadeType.ALL)
    private List<Account> accounts;
}
