package co.istad.easybanking.api.transaction;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;

public record DepositAndWithdrawMoney(
        Long accountNumber,
        BigDecimal amount,
        @NotNull
        Long StaffId){
}
