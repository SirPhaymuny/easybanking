package co.istad.easybanking.api.customer;

import co.istad.easybanking.api.user.UserDto;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.time.LocalDate;

public interface CustomerService {
    void createCustomer (CustomerCreateDto customerCreateDto);
    Page<CustomerCreateDto> findList(int pageNumber, int pageSize);
    CustomerCreateDto getListById(Long id);
    CustomerCreateDto getName(String firstname, String lastname, String nid);
    CustomerEditDto amendById(CustomerEditDto editDto);
    void deletedById(Long id);
}
