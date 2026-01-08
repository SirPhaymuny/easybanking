package co.istad.easybanking.api.customer;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    Customer fromCustomerCreateDto(CustomerCreateDto customerCreateDto);
    CustomerCreateDto fromCustomerToDto(Customer customer);
    void fromEditionDtoToCustomer(@MappingTarget Customer customer, CustomerEditDto customerEditDto);

}
