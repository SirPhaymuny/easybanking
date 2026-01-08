package co.istad.easybanking.api.user;

import co.istad.easybanking.api.staff.Staff;
import co.istad.easybanking.api.staff.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import co.istad.easybanking.Config.EmailService;
import co.istad.easybanking.api.customer.Customer;
import co.istad.easybanking.api.customer.CustomerRepository;
import org.thymeleaf.context.Context;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CustomerRepository customerRepository;
    private final EmailService emailService;
    private final StaffRepository staffRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    public void createNewUser(UserDto userDto) {
        if (userRepository.findByUserName(userDto.userName()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already existed");
        }
        Customer customers = customerRepository.findById(userDto.CustomerId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT, "Username not existed"));
        try {
            System.out.println(userDto);
            User user = userMapper.fromUserDto(userDto);
            if (user.getRole() == null) {
                user.setRole("USER");
            }
            user.setIsEnabled(true);
            user.setPassword(passwordEncoder.encode(userDto.password()));
            String template = "view-html";
            Context context = new Context();
            context.setVariable("user",user);
            emailService.sendEmail(userDto.email(), "Welcome to EasyBanking", "Your account has been successfully created.",context,template);
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Failed to create user: " + Objects.requireNonNull(e.getRootCause()).getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create user" + e);
        }
    }
    @Override
    public void createUserForStaff(CreateUserForStaff createUserForStaff) {
        Staff staff = staffRepository.findById(createUserForStaff.StaffId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.CONFLICT,"staff Id not found.")
        );
        if(staff.getUserId()==null) {
            if(!createUserForStaff.confirmPassword().equals(createUserForStaff.passWord())){
                throw new ResponseStatusException(HttpStatus.CONFLICT,"Password not match each other");
            }
            try {
                User user = userMapper.fromStaffDto(createUserForStaff);
                user.setStaffId(staff);
                user.setOtp(generateOtp());
                user.setEmail(staff.getEmail());
                user.setUserName(createUserForStaff.username());
                Set<Role> staffRole = new HashSet<>(staff.getRoles());
                user.setPassword(passwordEncoder.encode(createUserForStaff.passWord()));
                user.setRoles(staffRole);
                user.setExpirationDateTime(LocalDateTime.now());
                userRepository.save(user);
                String template = "verify-html";
                Context context = new Context();
                staff.setUserId(user);
                staffRepository.save(staff);
                context.setVariable("staff", staff);
                emailService.sendEmail(staff.getEmail(), "Welcome to EasyBanking", "Your Verify Your Otp.", context, template);
            } catch (DataIntegrityViolationException e) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Failed to create user: " + Objects.requireNonNull(e.getRootCause()).getMessage());
            }
        }else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Staff already have username : " + staff.getUserId().getUserName());
        }
    }
    @Override
    public List<UserListDto> getUserList() {
        List<User> listUser = userRepository.findAll();
        return userMapper.listUser(listUser);
    }
    @Override
    public UserListDto getUserByStaffId(Long id) {
        User foundUser = userRepository.findById(id).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found")
        );
        return userMapper.fromUserToDto(foundUser);
    }
    @Override
    public void enabledById(Long id) {
        User foundUser = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found")
        );
        if (foundUser.getIsEnabled()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already enabled");
        }else {
            foundUser.setIsEnabled(true);
            userRepository.save(foundUser);
        }
    }
    @Override
    public void disabledById(Long id) {
        User foundUser = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found")
        );
        if (!foundUser.getIsEnabled()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already disabled");
        }else {
            foundUser.setIsEnabled(false);
            userRepository.save(foundUser);
        }
    }
    private Integer generateOtp() {
        Random random = new Random();
        int range = 10000;
        int fraction = (int) (range * random.nextDouble());
        return fraction + 10000;
    }
}
