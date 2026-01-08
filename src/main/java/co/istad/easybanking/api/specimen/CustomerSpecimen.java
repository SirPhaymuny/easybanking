package co.istad.easybanking.api.specimen;


import co.istad.easybanking.api.customer.Customer;
import co.istad.easybanking.api.staff.Staff;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Builder
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Specimen")
public class CustomerSpecimen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageId;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customerId;

    private LocalDate createdDate;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Staff uploadBy;
}
