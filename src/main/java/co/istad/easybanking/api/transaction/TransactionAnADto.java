package co.istad.easybanking.api.transaction;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TransactionAnADto(
        String fTid,
        String transactionToken
) {
}
