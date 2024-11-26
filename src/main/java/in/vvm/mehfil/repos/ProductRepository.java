package in.vvm.mehfil.repos;

import in.vvm.mehfil.domain.Product;
import in.vvm.mehfil.domain.Vendor;
import in.vvm.mehfil.domain.Venue;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findFirstByVenue(Venue venue);

    Product findFirstByVendor(Vendor vendor);

    boolean existsByVenueId(Long id);

}
