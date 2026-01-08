package co.istad.easybanking.api.account;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "accountType", target = "accountType.categoryName")
    @Mapping(source = "customerId", target = "customer.customerId")
    Account fromAccountCreateDto(AccountCreateDto accountCreateDto);

    @Mapping(source = "accountType.categoryName", target = "accountType")
    AccountDetailDto DetailDtos(Account accounts);
}
