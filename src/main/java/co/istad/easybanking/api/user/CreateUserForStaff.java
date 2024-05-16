package co.istad.easybanking.api.user;

public record CreateUserForStaff(
        Long StaffId,
        String username,
        String passWord,
        String confirmPassword
) {
}
