package co.istad.easybanking.api.staff;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

public record StaffDto(
        @NotEmpty
        String firstName,
        @NotEmpty
        String lastName,
        @NotNull
        LocalDate dob,
        @NotNull
        @Email
        String email,
        Set<Integer> roleId )
{

}
