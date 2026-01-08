package co.istad.easybanking.api.account;

public record AccountDetailDto(
        Long accountNumber,
        String accountName,
        String accountType,
        String currency,
        Double balance) {

}
