package co.istad.easybanking.api.customer;


import co.istad.easybanking.api.specimen.FileUploadDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import co.istad.easybanking.base.BaseSuccess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final CustomerService customerService;

    @PreAuthorize("hasAuthority('SCOPE_staff:write') or hasAuthority('SCOPE_staff:update')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    BaseSuccess<?> createCustomer(@RequestBody @Valid CustomerCreateDto customerCreateDto) {
        customerService.createCustomer(customerCreateDto);
        return BaseSuccess.builder()
                .code(HttpStatus.CREATED.value())
                .message("Successfully Created")
                .status(true)
                .timestamp(LocalDateTime.now())
                .data(customerCreateDto)
                .build();
    }
    @PreAuthorize("hasAuthority('SCOPE_staff:read') or hasAuthority('SCOPE_staff:update')")
    @GetMapping("/getByCustomerId/{id}")
    BaseSuccess<?> getCustomerById(@PathVariable Long id) {
        return BaseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully uploaded")
                .status(true)
                .timestamp(LocalDateTime.now())
                .data(customerService.getListById(id))
                .build();
    }
    @PreAuthorize("hasAuthority('SCOPE_staff:read') or hasAuthority('SCOPE_staff:update')")
    @PostMapping("/searchCustomer")
    BaseSuccess<?> searchCustomer(@RequestBody CustomerSearch customerSearch) {
        return BaseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully uploaded")
                .status(true)
                .timestamp(LocalDateTime.now())
                .data(customerService.getName(customerSearch.firstname(),customerSearch.lastname(),customerSearch.nid()))
                .build();
    }
    @PreAuthorize("hasAuthority('SCOPE_staff:read') or hasAuthority('SCOPE_staff:update')")
    @PostMapping("/editCustomer")
    BaseSuccess<?> editCustomer(@RequestBody CustomerEditDto customerEditDto) {
        return BaseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully uploaded")
                .status(true)
                .timestamp(LocalDateTime.now())
                .data(customerService.amendById(customerEditDto))
                .build();
    }
    @PreAuthorize("hasAuthority('SCOPE_staff:read') or hasAuthority('SCOPE_staff:update')")
    @DeleteMapping("/deleteCustomer/{id}")
    BaseSuccess<?> editCustomer(@PathVariable Long id) {
        customerService.deletedById(id);
        return BaseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully uploaded")
                .status(true)
                .timestamp(LocalDateTime.now())
                .data("")
                .build();
    }
    @PreAuthorize("hasAuthority('SCOPE_staff:read') or hasAuthority('SCOPE_staff:update')")
    @GetMapping("/getCustomerPageable")
    BaseSuccess<?> getCustomerById(@RequestParam(required = false, defaultValue = "0") int pageNumber,
                                   @RequestParam(required = false, defaultValue = "4") int pageSize) {
        return BaseSuccess.builder()
                .code(HttpStatus.OK.value())
                .message("Successfully uploaded")
                .status(true)
                .timestamp(LocalDateTime.now())
                .data(customerService.findList(pageNumber,pageSize))
                .build();
    }

}
