package co.istad.easybanking.api.transaction;

import java.math.BigDecimal;

public record FundToken(
                String username,
                Long debitAccount,
                Long creditAccount,
                BigDecimal amount

) {
}
