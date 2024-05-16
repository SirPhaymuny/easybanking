package co.istad.easybanking.api.transaction;

import java.math.BigDecimal;
public record CashWithdraw(
        Long accountNumber,
        BigDecimal amount,
        Long staffId
) {
}
