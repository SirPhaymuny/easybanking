package co.istad.easybanking.api.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import java.time.LocalDateTime;
@Builder
public record AuthDto<T>(
        @NotNull
        Integer code,
        String message,
        LocalDateTime timestamp,
        Boolean status,
        T accessToken,
        T refreshToken){
}
