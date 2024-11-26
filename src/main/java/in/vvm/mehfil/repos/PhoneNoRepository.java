package in.vvm.mehfil.repos;

import in.vvm.mehfil.domain.Country;
import in.vvm.mehfil.domain.PhoneNo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PhoneNoRepository extends JpaRepository<PhoneNo, Long> {

    PhoneNo findFirstByCountry(Country country);

    boolean existsByPhoneNo(Long phoneNo);

}
