package co.istad.easybanking.api.specimen;
public record SpecimenUploadDto(
        Long customerId,
        Long StaffId,
        String base64
) {
}
