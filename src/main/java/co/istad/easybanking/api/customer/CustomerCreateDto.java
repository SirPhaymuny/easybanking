package co.istad.easybanking.api.customer;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CustomerCreateDto(
                @NotNull String firstName,
                @NotNull String lastName,
                @NotNull String nid,
                @NotNull LocalDate dateOfBirth,
                @NotNull String gender,
                @NotNull String phoneNumber) {

}