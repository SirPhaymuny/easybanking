package co.istad.easybanking.api.user;

import co.istad.easybanking.api.staff.StaffDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.istad.easybanking.api.auth.RegisterDto;

import java.util.List;

@Mapper(componentModel = "spring")

public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isEnabled", ignore = true)
    User fromUserDto(UserDto userDto);
    @Mapping(source = "username", target = "userName")
    @Mapping(source = "customerId", target = "CustomerId")
    UserDto fromRegisterDto(RegisterDto registerDto);
    List<UserListDto> listUser(List<User> users);
    UserListDto fromUserToDto(User user);
    User fromStaffDto(CreateUserForStaff createUserForStaff);
}
