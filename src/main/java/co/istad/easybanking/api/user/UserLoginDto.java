package co.istad.easybanking.api.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserLoginDto(
        @NotNull
        @NotEmpty
        String Username,
        @NotNull
        @NotEmpty
        String password
) {
}
