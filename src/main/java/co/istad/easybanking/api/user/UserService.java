package co.istad.easybanking.api.user;

import java.util.List;

public interface UserService {
    void createNewUser(UserDto userDto);
    void createUserForStaff(CreateUserForStaff createUserForStaff);
    List<UserListDto> getUserList();
    UserListDto getUserByStaffId(Long id);
    void enabledById(Long id);
    void disabledById(Long id);
}
