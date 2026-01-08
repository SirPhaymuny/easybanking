package co.istad.easybanking.api.account;

import co.istad.easybanking.base.BaseSuccess;

import java.util.List;

public interface AccountService {
    void createNewAccount(AccountCreateDto accountCreateDto);
    AccountEditDto editAccountInfo(AccountEditDto accountEditDto);
    void blockAccount(Long id);
    void unBlockAccount(Long id);
    void deleteById(Long id);
    List<AccountDetailDto> getAccountList();
}
