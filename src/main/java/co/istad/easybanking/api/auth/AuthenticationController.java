package co.istad.easybanking.api.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.istad.easybanking.api.user.UserRepository;
import co.istad.easybanking.base.BaseSuccess;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public ResponseEntity<BaseSuccess<?>> registerUser(@RequestBody @Valid RegisterDto registerDto) {
        try {
            System.out.println(registerDto);
            authenticationService.register(registerDto);
            return ResponseEntity.ok(BaseSuccess.builder()
                    .code(HttpStatus.OK.value())
                    .status(true)
                    .message("Successfully Register new User")
                    .timestamp(LocalDateTime.now())
                    .data(registerDto)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok(BaseSuccess.builder()
                    .code(HttpStatus.CONFLICT.value())
                    .status(false)
                    .message("Register Unsuccessful")
                    .timestamp(LocalDateTime.now())
                    .data(null)
                    .build());
        }
    }
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verifyToken")
    public ResponseEntity<BaseSuccess<?>> verifyToken(@RequestBody @Valid TokenDto tokenDto) {
        System.out.println(tokenDto);
        return ResponseEntity.ok(BaseSuccess.builder()
                .data(authenticationService.verifyToken(tokenDto))
                .status(true)
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.OK.value())
                .message("Successfully verify")
                .build());
    }
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public ResponseEntity<BaseSuccess<?>> login(@RequestBody @Valid LoginDto loginDto) {
        AuthDto authDto = authenticationService.login(loginDto);

        return ResponseEntity.ok(BaseSuccess.builder()
                .data(authDto)
                .status(true)
                .timestamp(LocalDateTime.now())
                .code(HttpStatus.OK.value())
                .message("Successfully login")
                .build());
    }
}