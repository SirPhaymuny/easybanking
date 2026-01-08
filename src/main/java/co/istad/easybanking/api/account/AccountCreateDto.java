package co.istad.easybanking.api.account;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AccountCreateDto(
                @NotNull
                Long customerId,
                @NotNull
                String accountType,
                @NotNull
                String currency,
                Double balance
) {}
