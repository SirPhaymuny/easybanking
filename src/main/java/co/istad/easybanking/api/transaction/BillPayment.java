package co.istad.easybanking.api.transaction;

import java.math.BigDecimal;

public record BillPayment(
                Long account,
                String billId,
                Long consumerCode,
                Long staffId) {
}
