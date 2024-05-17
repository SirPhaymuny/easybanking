package co.istad.easybanking.api.transaction;

import co.istad.easybanking.api.customer.CustomerCreateDto;
import org.springframework.data.domain.Page;

public interface TransactionService {
    TransactionAnADto transferBetweenAccount(FundTransferDto fundTransferDto);
    void depositMoneyToAccount(DepositAndWithdrawMoney depositMoney);
    void cashWithdraw(DepositAndWithdrawMoney withdrawMoney);
    void billPayment(BillPayment billPayment);
    String confirmPayment(TransactionAnADto Token);
    Page<FundTransferDto> findList(int pageNumber, int pageSize);
}
