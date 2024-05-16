package co.istad.easybanking.api.user;

import java.util.Set;

public record UserListDto(
        String userName,
        String email,
        Boolean isEnabled,
        Set<Role> roles
) {
}
