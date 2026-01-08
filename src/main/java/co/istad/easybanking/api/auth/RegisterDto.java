package co.istad.easybanking.api.auth;

import jakarta.validation.constraints.NotNull;
public record RegisterDto(
        @NotNull
        Long customerId,
        @NotNull
        String username,
        @NotNull
        String email,
        @NotNull
        String password,
        @NotNull
        String confirmPassword

) {
}
