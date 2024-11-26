package in.vvm.mehfil.repos;

import in.vvm.mehfil.domain.Address;
import in.vvm.mehfil.domain.Consumer;
import in.vvm.mehfil.domain.PhoneNo;
import in.vvm.mehfil.domain.User;
import in.vvm.mehfil.domain.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByConsumer(Consumer consumer);

    User findFirstByVendor(Vendor vendor);

    User findFirstByPhoneNumbes(PhoneNo phoneNo);

    User findFirstByAddress(Address address);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByConsumerId(Long id);

    boolean existsByVendorId(Long id);

}
