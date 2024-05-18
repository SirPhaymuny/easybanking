package co.istad.easybanking.init;

import co.istad.easybanking.api.account.Category;
import co.istad.easybanking.api.account.CategoryRespository;
import co.istad.easybanking.api.staff.StaffRepository;
import co.istad.easybanking.api.user.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Component
@RequiredArgsConstructor
public class DataInput {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final AuthorityRepository authorityRepository;
    private final CategoryRespository categoryRespository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
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
            Category category2 = new Category();
            category2.setCategoryName("Saving Account");
            Category category3 = new Category();
            category3.setCategoryName("Teen Account");
            categoryRespository.saveAll(Arrays.asList(category1,category2,category3));
        }

    }
}
