package co.istad.easybanking.Config;

import co.istad.easybanking.api.user.User;
import co.istad.easybanking.api.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("UserDetailServiceImpl : "+username);
        User user = userRepository.findByUserNameAndIsEnabled(
                username,
                true).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        log.info("User details: {}", user);
        System.out.println(user);
        return new CustomUserDetail(user);
    }
}
