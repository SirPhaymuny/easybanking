package co.istad.easybanking.api.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    @Override
    public void createCustomer(CustomerCreateDto customerCreateDto) {
        Customer foundByPhoneNumber = customerRepository.findCustomerByPhoneNumber(customerCreateDto.phoneNumber());
        if(foundByPhoneNumber!=null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Customer phone-number already existed");
        }else{
            try{
            Customer customer = customerMapper.fromCustomerCreateDto(customerCreateDto);
            customer.setFullName(customerCreateDto.firstName()+" "+customerCreateDto.lastName());
            customer.setCustomerId(generateRandomCustomerNumber());
            customerRepository.save(customer);
            }catch (DataIntegrityViolationException e) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Failed to create customer: " + Objects.requireNonNull(e.getRootCause()).getMessage());
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create customer", e);
            }
        }
    }
    @Override
    public Page<CustomerCreateDto> findList(int pageNumber, int pageSize) {
        Sort sortById = Sort.by(Sort.Direction.DESC,"id");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortById);
        Page<Customer> customers = customerRepository.findAll(pageable);
        return customers.map(customerMapper::fromCustomerToDto);
    }
    @Override
    public CustomerCreateDto getListById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer not found")
        );
        return customerMapper.fromCustomerToDto(customer);
    }
    @Override
    public CustomerCreateDto getName(String firstname, String lastname, String nid) {
        Customer foundCustomer = customerRepository.findCustomerByFirstNameAndLastNameAndNid(firstname,lastname,nid);
        if(foundCustomer==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Customer not found");
        }
        return customerMapper.fromCustomerToDto(foundCustomer);
    }
    @Override
    public CustomerEditDto amendById(CustomerEditDto editDto) {
        Customer foundCustomer = customerRepository.findById(editDto.id()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer not found")
        );
        customerMapper.fromEditionDtoToCustomer(foundCustomer,editDto);
        customerRepository.save(foundCustomer);
        return editDto;
    }
    @Override
    public void deletedById(Long id) {
        Customer foundCustomer = customerRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer not found")
        );
        try {
            customerRepository.delete(foundCustomer);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Failed to create customer: " + Objects.requireNonNull(e.getRootCause()).getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create customer", e);
        }
    }
    private Long generateRandomCustomerNumber() {
        Random random = new Random();
        long range = 1000L;
        long fraction = (long) (range * random.nextDouble());
        return fraction + 1000L;
    }
}
