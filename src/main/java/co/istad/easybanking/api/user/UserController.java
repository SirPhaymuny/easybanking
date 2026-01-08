package co.istad.easybanking.api.user;

import co.istad.easybanking.Config.CustomUserDetail;
import co.istad.easybanking.api.auth.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import co.istad.easybanking.base.BaseSuccess;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/Users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationService authenticationService;
    @PreAuthorize("hasAuthority('SCOPE_staff:write') or hasAuthority('SCOPE_staff:update')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    BaseSuccess<?> createUser(@RequestBody @Valid UserDto userDto) {
        System.out.println(userDto);
        userService.createNewUser(userDto);
        return BaseSuccess.builder()
                .data(userDto)
                .code(HttpStatus.OK.value())
                .message("Successful Listing")
                .timestamp(LocalDateTime.now())
                .status(true)
                .build();
    }

    @PreAuthorize("hasAuthority('SCOPE_staff:write') or hasAuthority('SCOPE_staff:update')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/createUserForStaff")
    BaseSuccess<?> createUserForStaff(@RequestBody @Valid CreateUserForStaff userDto) {
        System.out.println(userDto);
        userService.createUserForStaff(userDto);
        return BaseSuccess.builder()
                .data(userDto)
                .code(HttpStatus.OK.value())
                .message("Successful Listing")
                .timestamp(LocalDateTime.now())
                .status(true)
                .build();
    }
    @PreAuthorize("hasAuthority('SCOPE_staff:read')")
    @GetMapping
    BaseSuccess<?> getList() {
        return BaseSuccess.builder()
                .data(userRepository.findAll())
                .code(HttpStatus.OK.value())
                .message("Successful Listing")
                .timestamp(LocalDateTime.now())
                .status(true)
                .build();
    }
    @PreAuthorize("hasAuthority('SCOPE_staff:read')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getListById/{id}")
    BaseSuccess<?> getListById(@PathVariable Long id) {
        return BaseSuccess.builder()
                .data(userService.getUserByStaffId(id))
                .code(HttpStatus.OK.value())
                .message("Successful Listing")
                .timestamp(LocalDateTime.now())
                .status(true)
                .build();
    }
    @PreAuthorize("hasAuthority('SCOPE_staff:update')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/enabledById/{id}")
    BaseSuccess<?> enabledById(@PathVariable Long id) {
        userService.enabledById(id);
        return BaseSuccess.builder()
                .data("")
                .code(HttpStatus.OK.value())
                .message("Successful Listing")
                .timestamp(LocalDateTime.now())
                .status(true)
                .build();
    }
    @PreAuthorize("hasAuthority('SCOPE_staff:update')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/disabledById/{id}")
    BaseSuccess<?> disabledById(@PathVariable Long id) {
        userService.disabledById(id);
        return BaseSuccess.builder()
                .data("")
                .code(HttpStatus.OK.value())
                .message("Successful Listing")
                .timestamp(LocalDateTime.now())
                .status(true)
                .build();
    }
    @PreAuthorize("hasAuthority('SCOPE_staff:update') or hasAuthority('SCOPE_user:update')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verifyOtp/{id}")
    BaseSuccess<?> verifyOtp(@PathVariable Integer otp) {
        CustomUserDetail customUserDetail = new CustomUserDetail();
        Long userId = customUserDetail.getUserId();
        User foundUser = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Missing..")
        );
        if(authenticationService.confirmOtp(userId,otp)) {
            return BaseSuccess.builder()
                    .data("")
                    .code(HttpStatus.OK.value())
                    .message("Successful Verify")
                    .timestamp(LocalDateTime.now())
                    .status(true)
                    .build();
        }else {
            return BaseSuccess.builder()
                    .data("")
                    .code(HttpStatus.OK.value())
                    .message("")
                    .timestamp(LocalDateTime.now())
                    .status(true)
                    .build();
        }
    }
}
