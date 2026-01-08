package co.istad.easybanking.api.customer;

import java.time.LocalDate;

public record CustomerEditDto(
        Long id,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String phoneNumber){
}
