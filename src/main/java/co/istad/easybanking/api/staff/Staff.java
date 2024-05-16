package co.istad.easybanking.api.staff;

import java.time.LocalDate;
import java.util.Set;
import co.istad.easybanking.api.user.Role;
import co.istad.easybanking.api.user.User;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "staff_id")
    private Long staffId;

    private String firstName;

    private String lastName;

    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dob;

    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "staff_roles", joinColumns = @JoinColumn(name = "staff_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @OneToOne(fetch = FetchType.EAGER)
    private User userId;
}
