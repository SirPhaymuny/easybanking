package co.istad.easybanking.api.account;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AccountRespository extends JpaRepository<Account, Long> {

    @Query("SELECT MAX(e.accountNumber) FROM Account e")
    Long findMaxId();

    @Transactional
    @Modifying
    @Query("""
                DELETE Account AS u
                WHERE u.accountName = ?1
            """)
    void deletePermanently(String username);

    Account findAccountByAccountNumberAndAccountStatus(Long id, Boolean accountStatus);
}
