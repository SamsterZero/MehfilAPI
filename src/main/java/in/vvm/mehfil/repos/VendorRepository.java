package in.vvm.mehfil.repos;

import in.vvm.mehfil.domain.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VendorRepository extends JpaRepository<Vendor, Long> {
}
