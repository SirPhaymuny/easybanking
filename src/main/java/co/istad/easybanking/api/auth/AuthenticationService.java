package co.istad.easybanking.api.auth;


import co.istad.easybanking.base.BaseSuccess;

public interface AuthenticationService {
    void register(RegisterDto registerDto);
    String verifyToken(TokenDto tokenDto);
    AuthDto login(LoginDto loginDto);
    BaseSuccess<?> changPassword(ChangPDto changPDto);
    BaseSuccess<?> resetPassword(ResetPasswordDto resetPasswordDto);
    Boolean confirmOtp(Long id, Integer opt);
}
