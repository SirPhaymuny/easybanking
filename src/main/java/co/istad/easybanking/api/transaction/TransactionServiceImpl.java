package co.istad.easybanking.api.transaction;

import co.istad.easybanking.jwtGenerater.JwtTokenService;
import co.istad.easybanking.api.account.Account;
import co.istad.easybanking.api.account.AccountRespository;
import co.istad.easybanking.api.staff.Staff;
import co.istad.easybanking.api.staff.StaffRepository;
import com.nimbusds.jose.JOSEException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final AccountRespository accountRespository;
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final StaffRepository staffRepository;
    private final JwtTokenService jwtTokenService;
    private final JwtDecoder jwtDecoder;

    @Override
    public TransactionAnADto transferBetweenAccount(FundTransferDto fundTransferDto) {
        Account findDebitAccount = accountRespository
                .findAccountByAccountNumberAndAccountStatus(fundTransferDto.debitAccount(), true);
        if (findDebitAccount == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Missing Debit account");
        }
        Account findCredittAccount = accountRespository
                .findAccountByAccountNumberAndAccountStatus(fundTransferDto.creditAccount(), true);
        if (findCredittAccount == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Missing Credit account");
        }
        Staff staff = staffRepository.findById(fundTransferDto.StaffId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT,"Staff Id not valid")
        );
        int compareResult = findDebitAccount.getBalance().compareTo(fundTransferDto.amount());
        if (compareResult < 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Debit Amount is less than transfer amount");
        }

        if (fundTransferDto.transactionDesc().length() > 90) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Transaction Desc must less than or equal to 90 characters");
        }
        String ft = generateFt();
        Transaction transaction = transactionMapper.fromFundDtoToTrans(fundTransferDto);

        transaction.setTransactionId(ft);
        transaction.setDebitAccount(findDebitAccount);
        transaction.setCreditAccount(findCredittAccount);
        transaction.setStaffId(staff);
        transaction.setSender(findDebitAccount.getAccountName());

        transaction.setAuthorize(false);
        transaction.setTransferDate(LocalDateTime.now());
        transaction.setTransactionType("Account Transfer");

        transactionRepository.save(transaction);
        System.out.println(transaction.getDebitAccount().getId());
        FundToken fundTokenMap = transactionMapper.fromFundDtoToToken(transaction);
        System.out.println(fundTokenMap);
        String fundToken = jwtTokenService.generateFundToken(fundTokenMap, "Fund Token", ft);

        return TransactionAnADto.builder()
                .transactionToken(fundToken)
                .build();
    }
    @Override
    public void depositMoneyToAccount(DepositAndWithdrawMoney depositMoney) {
        Staff staff = staffRepository.findById(depositMoney.StaffId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Staff Id not found"));
        Account account = accountRespository.findAccountByAccountNumberAndAccountStatus(depositMoney.accountNumber(),
                true);
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account not found or not activated");
        } else {
            account.setBalance(depositMoney.amount());
            accountRespository.save(account);
        }
    }
    @Override
    public void cashWithdraw(DepositAndWithdrawMoney withdrawMoney) {

    }
    @Override
    public void billPayment(BillPayment billPayment) {
        try {
            Account account = accountRespository.findAccountByAccountNumberAndAccountStatus(billPayment.account(), true);
            Staff staff = staffRepository.findById(billPayment.staffId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.CONFLICT, "Staff Id is invalid"));
            if (account == null) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Account not found or Deactivated");
            } else {
                if (!billPayment.billId().equals("EDC") && !billPayment.billId().equals("PPWS")) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Bill Id currently only support EDC and PPWS");
                } else {
                    int compareBillAmt = billPayment.amount().compareTo(BigDecimal.valueOf(0));
                    int compareResult = account.getBalance().compareTo(billPayment.amount());
                    if (compareResult < 0) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Debit Amount is less than transfer amount");
                    }
                    if (compareBillAmt < 0 || compareBillAmt == 0) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Bill amount less or equal to Zero");
                    }
                }
            }
            Transaction transaction = transactionMapper.fromBillToTransaction(billPayment);
            String txn = generateFt();
            transaction.setTransactionId(txn);
            transaction.setTransactionType("Bill Payment");
            transaction.setAuthorize(false);
            transaction.setTransferDate(LocalDateTime.now());
            transactionRepository.save(transaction);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @Override
    public String confirmPayment(TransactionAnADto token) {
            System.out.println("into else");
            try {
                /*
                 * Authentication auth = new
                 * BearerTokenAuthenticationToken(token.transactionToken());
                 * Authentication authentication = jwtAuthenticationProvider.authenticate(auth);
                 */
                jwtTokenService.JwtValidate(token.transactionToken());
                Jwt jwtClaimsSet = jwtDecoder.decode(token.transactionToken());
                System.out.println(jwtClaimsSet.getSubject());
                String detail = jwtClaimsSet.getClaims().toString();
                String associated = jwtClaimsSet.getClaim("Associated");
                String[] values = associated.split("\\|");
                Long debitAc = Long.valueOf(values[0]);
                Long creditAc = Long.valueOf(values[1]);
                BigDecimal amt = BigDecimal.valueOf(Long.parseLong(values[2])).setScale(2, RoundingMode.UNNECESSARY);
                String user = values[3];
                String ft = values[4];
                Transaction transaction = transactionRepository.findTransactionByTransactionId(ft);
                if (transaction == null) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Invalid Transaction Id");
                }
                if (transaction.getAuthorize()) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Transaction Id already Used");
                }
                if (!user.equals(transaction.getDebitAccount().getAccountName())) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is not match");
                } else {
                    if (!debitAc.equals(transaction.getDebitAccount().getAccountNumber())
                            || !creditAc.equals(transaction.getCreditAccount().getAccountNumber())) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT,
                                "Credit Account or Debit Account not match");
                    } else {
                        if (!amt.equals(transaction.getAmount())) {
                            throw new ResponseStatusException(HttpStatus.CONFLICT, "Amount not match");
                        } else {
                            transaction.setAuthorize(true);
                            transactionRepository.save(transaction);
                            Account account = accountRespository.findAccountByAccountNumberAndAccountStatus(creditAc,
                                    true);
                            account.setBalance(amt);
                            accountRespository.save(account);
                        }
                    }
                }
                return ft;
            } catch (ExpiredJwtException | ParseException | JOSEException e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getLocalizedMessage());
            }
    }
    @Override
    public Page<FundTransferDto> findList(int pageNumber, int pageSize) {
        Sort sortById = Sort.by(Sort.Direction.DESC,"id");
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sortById);
        Page<Transaction> transactions = transactionRepository.findAll(pageable);
        return transactions.map(transactionMapper::fromTransactionToDto);
    }
    public String generateFt() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(9);

        // Generate 9 random characters
        for (int i = 0; i < 9; i++) {
            // Append a random character from CHARACTERS string
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return "FT" + sb.toString();
    }
}
