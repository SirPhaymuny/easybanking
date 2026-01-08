package co.istad.easybanking.api.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  User findByUserName(String username);
  Optional<User> findByUserNameAndIsEnabled(String username, boolean isEnabled);
  Optional<User> findByEmail(String email);
  User findByIdAndIsEnabled(Long id, Boolean found);

}

