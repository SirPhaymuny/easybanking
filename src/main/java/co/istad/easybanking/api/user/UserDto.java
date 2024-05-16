package co.istad.easybanking.api.user;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserDto(
                @NotNull String userName,
                @NotNull
                String email,
                String role,
                @NotNull String password,
                @NotNull Long CustomerId
){
}
