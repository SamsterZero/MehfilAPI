package in.vvm.mehfil.repos;

import in.vvm.mehfil.domain.Address;
import in.vvm.mehfil.domain.Pincode;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepository extends JpaRepository<Address, Long> {

    Address findFirstByPincode(Pincode pincode);

}
