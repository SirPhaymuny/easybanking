package co.istad.easybanking.api.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long>{
    Customer findCustomerByPhoneNumber(String phoneNumber);
    Customer findCustomerByFirstNameAndLastNameAndNid(String firstName, String lastName, String nid);
    Boolean existsByFullName(String fullName);
}