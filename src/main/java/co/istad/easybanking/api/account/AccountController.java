package co.istad.easybanking.api.account;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import co.istad.easybanking.api.customer.Customer;
import co.istad.easybanking.api.customer.CustomerRepository;
import co.istad.easybanking.base.BaseSuccess;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
        private final AccountRespository accountRespository;
        private final AccountService accountService;

        @PreAuthorize("hasAuthority('SCOPE_staff:read') or hasAuthority('SCOPE_staff:update')")
        @GetMapping
        BaseSuccess<?> getAccountList() {
                return BaseSuccess.builder()
                                .code(HttpStatus.CREATED.value())
                                .message("Successfully List")
                                .timestamp(LocalDateTime.now())
                                .status(true)
                                .data(accountService.getAccountList())
                                .build();
        }
        @PreAuthorize("hasAuthority('SCOPE_staff:write') or hasAuthority('SCOPE_staff:update')")
        @ResponseStatus(HttpStatus.CREATED)
        @PostMapping
        BaseSuccess<?> createAccount(@RequestBody @Valid AccountCreateDto accountCreateDto) {
                accountService.createNewAccount(accountCreateDto);
                return BaseSuccess.builder()
                                .code(HttpStatus.CREATED.value())
                                .message("Successfully create account")
                                .timestamp(LocalDateTime.now())
                                .status(true)
                                .data(accountCreateDto)
                                .build();
        }

        @ResponseStatus(HttpStatus.NO_CONTENT)
        @DeleteMapping("/{accountname}")
        void deletedAccount(@PathVariable String accountname) {
                accountRespository.deletePermanently(accountname);
        }
        @PreAuthorize("hasAuthority('SCOPE_staff:write') or hasAuthority('SCOPE_staff:update')")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        @DeleteMapping("/deleteCustomer/{id}")
        BaseSuccess<?> createAccount(@PathVariable Long id) {
                accountService.deleteById(id);
                return BaseSuccess.builder()
                        .code(HttpStatus.CREATED.value())
                        .message("Successfully create account")
                        .timestamp(LocalDateTime.now())
                        .status(true)
                        .data("")
                        .build();
        }
        @PreAuthorize("hasAuthority('SCOPE_staff:write') or hasAuthority('SCOPE_staff:update')")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        @PostMapping("/blockById/{id}")
        BaseSuccess<?> blockById(@PathVariable Long id) {
                accountService.blockAccount(id);
                return BaseSuccess.builder()
                        .code(HttpStatus.CREATED.value())
                        .message("Successfully create account")
                        .timestamp(LocalDateTime.now())
                        .status(true)
                        .data("")
                        .build();
        }
        @PreAuthorize("hasAuthority('SCOPE_staff:write') or hasAuthority('SCOPE_staff:update')")
        @ResponseStatus(HttpStatus.NO_CONTENT)
        @PostMapping("/unblockById/{id}")
        BaseSuccess<?> unblockById(@PathVariable Long id) {
                accountService.unBlockAccount(id);
                return BaseSuccess.builder()
                        .code(HttpStatus.CREATED.value())
                        .message("Successfully create account")
                        .timestamp(LocalDateTime.now())
                        .status(true)
                        .data("")
                        .build();
        }

}
