package co.istad.easybanking.api.transaction;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    FundToken fromFundDtoToToken(FundTransferDto fundTransferDto);

    @Mapping(source = "debitAccount", target = "debitAccount.accountNumber")
    @Mapping(source = "creditAccount", target = "creditAccount.accountNumber")
    Transaction fromFundDtoToTrans(FundTransferDto fundTransferDto);
    @Mapping(source = "debitAccount.accountNumber", target = "debitAccount")
    @Mapping(source = "creditAccount.accountNumber", target = "creditAccount")
    FundTransferDto fromTransactionToDto(Transaction transaction);

    @Mapping(source = "staffId", target = "staffId.staffId")
    Transaction fromBillToTransaction(BillPayment billPayment);
}
