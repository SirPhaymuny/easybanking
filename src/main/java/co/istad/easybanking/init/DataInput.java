package co.istad.easybanking.init;

import co.istad.easybanking.api.account.Account;
import co.istad.easybanking.api.account.AccountRespository;
import co.istad.easybanking.api.account.Category;
import co.istad.easybanking.api.account.CategoryRespository;
import co.istad.easybanking.api.customer.Customer;
import co.istad.easybanking.api.customer.CustomerRepository;
import co.istad.easybanking.api.staff.StaffRepository;
import co.istad.easybanking.api.transaction.Consumer;
import co.istad.easybanking.api.transaction.ConsumerRepository;
import co.istad.easybanking.api.user.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.IntStream;

import static javax.swing.text.html.parser.DTDConstants.NAMES;
import static org.hibernate.annotations.UuidGenerator.Style.RANDOM;

@Component
@RequiredArgsConstructor
public class DataInput {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final AuthorityRepository authorityRepository;
    private final CategoryRespository categoryRespository;
    private final CustomerRepository customerRepository;
    private final AccountRespository accountRespository;
    private final ConsumerRepository consumerRepository;
    private static final Random RANDOM = new Random();
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String[] NAMES = {
            "John Doe", "Jane Doe", "Alice", "Bob", "Charlie", "David", "Eve", "Frank",
            "Grace", "Hank", "Ivy", "Jack", "Kathy", "Leo", "Mona", "Nick", "Olivia",
            "Paul", "Quincy", "Rose", "Sam", "Tina", "Uma", "Victor", "Wendy", "Xander",
            "Yara", "Zane", "Ann", "Ben", "Cara", "Dan", "Elle", "Finn", "Gina", "Harry",
            "Iris", "Jake", "Kelly"
    };
    @PostConstruct
    void init() {
        if (roleRepository.findAll().isEmpty() && authorityRepository.findAll().isEmpty()) {
            Authority userWrite = new Authority();
            userWrite.setName("user:write");
            Authority userUpdate = new Authority();
            userUpdate.setName("user:update");
            Authority userRead = new Authority();
            userRead.setName("user:read");

            Authority staffWrite = new Authority();
            staffWrite.setName("staff:write");
            Authority staffUpdate = new Authority();
            staffUpdate.setName("staff:update");
            Authority staffRead = new Authority();
            staffRead.setName("staff:read");
            Authority staffDelete = new Authority();
            staffDelete.setName("staff:delete");

            authorityRepository.saveAll(Arrays.asList(userWrite, userUpdate, userRead, staffWrite, staffUpdate, staffRead, staffDelete));
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            adminRole.setAuthorities(Arrays.asList(userWrite, userUpdate, userRead, staffWrite, staffUpdate, staffRead, staffDelete));

            Role staffRole = new Role();
            staffRole.setName("STAFF");
            staffRole.setAuthorities(Arrays.asList(staffWrite, staffUpdate, staffRead, staffDelete));

            Role userRole = new Role();
            userRole.setName("USER");
            userRole.setAuthorities(Arrays.asList(userWrite, userUpdate, userRead));

            roleRepository.saveAll(Arrays.asList(adminRole, staffRole, userRole));
        }
        if (userRepository.findByUserName("admin") == null) {
            Set<Role> setRole = new HashSet<>(roleRepository.findAll());
            User userAdmin = User.builder()
                    .email("admin@gmail.com")
                    .userName("admin")
                    .role("admin")
                    .roles(setRole)
                    .password(passwordEncoder.encode("123456"))
                    .isEnabled(true)
                    .build();
            userRepository.save(userAdmin);
        }

        if(categoryRespository.findAll().isEmpty()){
            Category category1 = new Category();
            category1.setCategoryName("Elite Account");
            categoryRespository.save(category1);
            Category category2 = new Category();
            category2.setCategoryName("Saving Account");
            categoryRespository.save(category2);
            Category category3 = new Category();
            category3.setCategoryName("Teen Account");
            categoryRespository.save(category3);
            Category category4 = new Category();
            category4.setCategoryName("Suspend Account");
            categoryRespository.save(category4);
        }

        System.out.println(!customerRepository.existsByFullName("EDC of Cambodia"));
        if(!customerRepository.existsByFullName("EDC of Cambodia")){
            Customer EDCCustomer = new Customer();
            EDCCustomer.setFullName("EDC of Cambodia");
            EDCCustomer.setCustomerId(6666L);
            EDCCustomer.setGender("CO");
            EDCCustomer.setPhoneNumber("123456789");
            EDCCustomer.setNid("EDC");
            EDCCustomer.setFirstName("EDC");
            EDCCustomer.setLastName("of Cambodia");
            customerRepository.save(EDCCustomer);
            Account accountEDC = new Account();
            accountEDC.setCustomer(EDCCustomer);
            accountEDC.setAccountType(categoryRespository.findCategoryByCategoryName("Suspend Account"));
            accountEDC.setAccountStatus(true);
            accountEDC.setAccountNumber(555555555L);
            accountEDC.setCurrency("KHR");
            accountRespository.save(accountEDC);
        }

        if(!customerRepository.existsByFullName("PPWS of Cambodia")){
            Customer PPWSCustomer = new Customer();
            PPWSCustomer.setFullName("PPWS of Cambodia");
            PPWSCustomer.setCustomerId(6666L);
            PPWSCustomer.setGender("CO");
            PPWSCustomer.setPhoneNumber("123456781");
            PPWSCustomer.setNid("PPWS");
            PPWSCustomer.setFirstName("PPWS");
            PPWSCustomer.setLastName("of Cambodia");
            customerRepository.save(PPWSCustomer);
            Account accountPPWS = new Account();
            accountPPWS.setCustomer(PPWSCustomer);
            accountPPWS.setAccountType(categoryRespository.findCategoryByCategoryName("Suspend Account"));
            accountPPWS.setAccountStatus(true);
            accountPPWS.setAccountNumber(555555556L);
            accountPPWS.setCurrency("KHR");
            accountRespository.save(accountPPWS);
        }

        if(consumerRepository.findAll().isEmpty()){
            if (consumerRepository.findAll().isEmpty()) {
                    IntStream.range(0, 40).forEach(i -> {
                        Consumer consumer = new Consumer();
                        consumer.setBillId("PPWS");
                        consumer.setConsumerName(generateRandomName());
                        consumer.setAmount(generateRandomAmount());
                        consumer.setConsumerId(generateId());
                        consumerRepository.save(consumer);
                        Consumer consumer1 = new Consumer();
                        consumer1.setBillId("EDC");
                        consumer1.setConsumerName(generateRandomName());
                        consumer1.setAmount(generateRandomAmount());
                        consumer1.setConsumerId(generateId());
                        consumerRepository.save(consumer1);
                    });
            }
        }
    }
    String generateRandomName() {
        return NAMES[RANDOM.nextInt(NAMES.length)];
    }
    BigDecimal generateRandomAmount() {
        return BigDecimal.valueOf(10000 + RANDOM.nextInt(10001));
    }
    public Long generateId() {
        long fraction;
        do {
            Random random = new Random();
            long range = 10000L;
            fraction = (long) (range * random.nextDouble());
            return fraction + 10000L;
        }while(consumerRepository.findConsumerByConsumerId(fraction)==null);
    }
}
