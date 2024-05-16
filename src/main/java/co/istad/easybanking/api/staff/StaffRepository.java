package co.istad.easybanking.api.staff;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    Staff findStaffByEmail(String email);
    Staff findStaffByStaffId(Long staffId);
}
