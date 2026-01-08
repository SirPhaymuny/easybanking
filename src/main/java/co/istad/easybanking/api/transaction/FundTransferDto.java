package co.istad.easybanking.api.transaction;
import java.math.BigDecimal;

public record FundTransferDto(
        Long debitAccount,
        Long creditAccount,
        String currency,
        BigDecimal amount,
        String transactionDesc,
        Long StaffId
) {
}
