package co.istad.easybanking.api.account;

import co.istad.easybanking.api.customer.Customer;
import co.istad.easybanking.api.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRespository accountRespository;
    private final CustomerRepository customerRepository;
    private final CategoryRespository categoryRespository;
    private final AccountMapper accountMapper;

    @Override
    public void createNewAccount(AccountCreateDto accountCreateDto) {

        Customer customer = customerRepository.findById(accountCreateDto.customerId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Customer Id not found")
        );
        Category category = categoryRespository.findCategoryByCategoryName(accountCreateDto.accountType());
        if (category == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Category not found");
        }
        Account account = accountMapper.fromAccountCreateDto(accountCreateDto);
        account.setAccountType(category);
        account.setAccountName(customer.getFullName());
        account.setCustomer(customer);
        Long accountNew = generateRandomAccountNumber();
        account.setAccountNumber(accountNew);
        account.setAccountStatus(true);
        accountRespository.save(account);
    }

    @Override
    public AccountEditDto editAccountInfo(AccountEditDto accountEditDto) {
        return null;
    }

    @Override
    public void blockAccount(Long id) {
        Account foundAccount = accountRespository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Account not found")
        );
        if (!foundAccount.getAccountStatus()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This account already blocked");
        } else {
            foundAccount.setAccountStatus(false);
            accountRespository.save(foundAccount);
        }
    }

    @Override
    public void unBlockAccount(Long id) {
        Account foundAccount = accountRespository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Account not found")
        );
        if (foundAccount.getAccountStatus()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This account already un-blocked");
        } else {
            foundAccount.setAccountStatus(true);
            accountRespository.save(foundAccount);
        }
    }

    @Override
    public void deleteById(Long id) {
        Account foundAccount = accountRespository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Account not found")
        );
        try {
            accountRespository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Failed to deleted account: " + Objects.requireNonNull(e.getRootCause()).getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to deleted customer", e);
        }
    }

    @Override
    public List<AccountDetailDto> getAccountList() {
        List<Account> getAllAccount = accountRespository.findAll();
        List<AccountDetailDto> listAccount = new ArrayList<>();
        getAllAccount.forEach(
                account -> {
                    AccountDetailDto accountDetailDto = accountMapper.DetailDtos(account);
                    listAccount.add(accountDetailDto);
                }
        );
        System.out.println(listAccount);
        return listAccount;
    }

    private Long generateRandomAccountNumber() {
        Random random = new Random();
        Long newAccount;
        Account account;

        do {
            long range = 1000000000L;
            long fraction = (long) (range * random.nextDouble());
            newAccount = fraction + 1000000000L;

            // Check if the account number already exists
            account = accountRespository.findAccountByAccountNumberAndAccountStatus(newAccount, true);
        } while (account != null);
        return newAccount;
    }
}
