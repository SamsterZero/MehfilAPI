package in.vvm.mehfil.repos;

import in.vvm.mehfil.domain.City;
import in.vvm.mehfil.domain.Pincode;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PincodeRepository extends JpaRepository<Pincode, Long> {

    Pincode findFirstByCity(City city);

    boolean existsByPincodeIgnoreCase(String pincode);

}
