package co.istad.easybanking.api.staff;

import co.istad.easybanking.base.BaseSuccess;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/staff")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;
    @PreAuthorize("hasAuthority('SCOPE_staff:write') or hasAuthority('SCOPE_staff:update')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    BaseSuccess<?> createNewStaff(@Valid @RequestBody StaffDto staffDto){
        System.out.println(" In Controller ");
       staffService.createNewStaff(staffDto);
       return BaseSuccess.builder()
               .code(HttpStatus.OK.value())
               .status(true)
               .timestamp(LocalDateTime.now())
               .message("Successfully Created")
               .data(staffDto)
               .build();
    }
    @PreAuthorize("hasAuthority('SCOPE_staff:read')")
    @GetMapping("/getStaff")
    public BaseSuccess<?> listAllStaff(){
        return BaseSuccess.builder()
                .code(HttpStatus.OK.value())
                .status(true)
                .timestamp(LocalDateTime.now())
                .message("Successfully Created")
                .data(staffService.listStaff())
                .build();
    }
    @PreAuthorize("hasAuthority('SCOPE_staff:read')")
    @GetMapping("/getByStaffId/{id}")
    public BaseSuccess<?> getStaffById(@PathVariable Long id){
        return BaseSuccess.builder()
                .code(HttpStatus.OK.value())
                .status(true)
                .timestamp(LocalDateTime.now())
                .message("Successfully Created")
                .data(staffService.listById(id))
                .build();
    }
}
