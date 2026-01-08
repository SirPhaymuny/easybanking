package co.istad.easybanking.api.transaction;

import co.istad.easybanking.base.BaseSuccess;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PreAuthorize("hasAuthority('SCOPE_staff:write') or hasAuthority('SCOPE_staff:update')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BaseSuccess<?> transactionBetweenAccount(@RequestBody @Valid FundTransferDto transactionAnADto){
        TransactionAnADto fundTransferDto = transactionService.transferBetweenAccount(transactionAnADto);
        return BaseSuccess.builder()
                .code(HttpStatus.CREATED.value())
                .status(true)
                .timestamp(LocalDateTime.now())
                .message("Transaction Successful")
                .data(fundTransferDto)
                .build();
    }
    @PreAuthorize("hasAuthority('SCOPE_staff:write') or hasAuthority('SCOPE_staff:update')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/confirm")
    public BaseSuccess<?> confirmTransaction(@RequestBody @Valid TransactionAnADto transactionAnADto){
       String ft = transactionService.confirmPayment(transactionAnADto);
        return BaseSuccess.builder()
                .code(HttpStatus.CREATED.value())
                .status(true)
                .timestamp(LocalDateTime.now())
                .message("Transaction Successful")
                .data(ft)
                .build();
    }
    @PreAuthorize("hasAuthority('SCOPE_staff:write') or hasAuthority('SCOPE_staff:update')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/depositMoney")
    public BaseSuccess<?> depositMoney(@RequestBody @Valid DepositAndWithdrawMoney transactionAnADto){
        transactionService.depositMoneyToAccount(transactionAnADto);
        return BaseSuccess.builder()
                .code(HttpStatus.CREATED.value())
                .status(true)
                .timestamp(LocalDateTime.now())
                .message("Transaction Successful")
                .data(" ")
                .build();
    }
    @PreAuthorize("hasAuthority('SCOPE_staff:write') or hasAuthority('SCOPE_staff:update')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/billPayment")
    public BaseSuccess<?> billPayment(@RequestBody @Valid BillPayment transactionAnADto){
        String ft = transactionService.billPayment(transactionAnADto);
        return BaseSuccess.builder()
                .code(HttpStatus.CREATED.value())
                .status(true)
                .timestamp(LocalDateTime.now())
                .message("Transaction Successful")
                .data(ft)
                .build();
    }
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/getTransactionList")
    public BaseSuccess<?> getListPageable(@RequestParam(required = false, defaultValue = "0") int pageNumber,
                                          @RequestParam(required = false, defaultValue = "4") int pageSize){
        return BaseSuccess.builder()
                .code(HttpStatus.CREATED.value())
                .status(true)
                .timestamp(LocalDateTime.now())
                .message("Transaction Successful")
                .data(transactionService.findList(pageNumber,pageSize))
                .build();
    }
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/getTransactionById/{id}")
    BaseSuccess<?> createAccount(@PathVariable String id) {
        return BaseSuccess.builder()
                .code(HttpStatus.CREATED.value())
                .message("Successfully create account")
                .timestamp(LocalDateTime.now())
                .status(true)
                .data(transactionService.listByFtId(id))
                .build();
    }
}
