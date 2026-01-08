package co.istad.easybanking.api.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record TokenDto(
        @NotNull
        String token
) {
}
