package co.istad.easybanking.api.auth;

public record ResetPasswordDto(
        String username,
        String email
) {
}
