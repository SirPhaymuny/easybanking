package co.istad.easybanking.api.staff;

import java.util.List;
import java.util.Optional;

public interface StaffService {
    void createNewStaff(StaffDto staffDto);
    List<StaffDto> listStaff();
   StaffDto listById(Long StaffId);
}
