package co.istad.easybanking.api.transaction;

import co.istad.easybanking.api.customer.CustomerCreateDto;
import org.springframework.data.domain.Page;

public interface TransactionService {
    TransactionAnADto transferBetweenAccount(FundTransferDto fundTransferDto);
    void depositMoneyToAccount(DepositAndWithdrawMoney depositMoney);
    void cashWithdraw(DepositAndWithdrawMoney withdrawMoney);
    String billPayment(BillPayment billPayment);
    String confirmPayment(TransactionAnADto Token);
    FundTransferDto listByFtId(String ft);
    Page<FundTransferDto> findList(int pageNumber, int pageSize);
}
