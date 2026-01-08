package co.istad.easybanking.api.staff;

import co.istad.easybanking.api.user.Role;
import co.istad.easybanking.api.user.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService{
    private final StaffRepository staffRepository;
    private final StaffMapper staffMapper;
    private final RoleRepository roleRepository;
    @Override
    public void createNewStaff(StaffDto staffDto) {
        if(staffRepository.findStaffByEmail(staffDto.email())!=null){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already existed");
        }
        List<Role> roles = roleRepository.findAllById(staffDto.roleId());
        Set<Role>  staffRole = new HashSet<>(roles);

        Staff staffs = staffMapper.fromStaffDto(staffDto);
        staffs.setFullName(staffDto.firstName()+" "+staffDto.lastName());
        staffs.setRoles(staffRole);
        System.out.println(staffs);
        try{
            staffs.setStaffId(generateRandomStaffId());
            staffRepository.save(staffs);
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error internal server : "+exception.getMessage());
        }
    }
    @Override
    public List<StaffDto> listStaff() {
        List<Staff> allStaff = staffRepository.findAll();
        return staffMapper.fromListStaff(allStaff);
    }
    @Override
    public StaffDto listById(Long staffId) {
        Staff foundStaff = staffRepository.findStaffByStaffId(staffId);
        if(foundStaff==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Staff not found");
        }
        return staffMapper.fromStaffToDto(foundStaff);
    }
    private Long generateRandomStaffId() {
        Random random = new Random();
        long range = 1000L;
        long fraction = (long) (range * random.nextDouble());
        return fraction + 1000L;
    }
}
