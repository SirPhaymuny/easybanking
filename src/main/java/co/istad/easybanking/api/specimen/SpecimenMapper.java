package co.istad.easybanking.api.specimen;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SpecimenMapper {
    @Mapping(source = "StaffId", target = "uploadBy.staffId")
    @Mapping(source = "customerId", target = "customerId.customerId")
    CustomerSpecimen fromCusToDto(SpecimenUploadDto specimenUploadDto);
}
