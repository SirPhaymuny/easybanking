package co.istad.easybanking.api.specimen;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecimenRepository extends JpaRepository<CustomerSpecimen, Long> {
}
