package in.vvm.mehfil.repos;

import in.vvm.mehfil.domain.Address;
import in.vvm.mehfil.domain.Venue;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VenueRepository extends JpaRepository<Venue, Long> {

    Venue findFirstByAddress(Address address);

}
