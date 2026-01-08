package co.istad.easybanking.api.staff;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface StaffMapper {
    @Mapping(target = "fullName", ignore = true)
    Staff fromStaffDto(StaffDto staffDto);
    @Mapping(target = "fullName", ignore = true)
    List<StaffDto> fromListStaff(List<Staff> staff);
    StaffDto fromStaffToDto(Staff staff);
}
