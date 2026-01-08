package co.istad.easybanking.api.auth;

public record ChangPDto(
        String username,
        String email,
        String pwd,
        String newPass,
        String ConfirmPass
) {
}
