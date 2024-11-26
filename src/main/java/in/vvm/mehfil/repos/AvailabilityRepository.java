package in.vvm.mehfil.repos;

import in.vvm.mehfil.domain.Availability;
import in.vvm.mehfil.domain.Venue;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AvailabilityRepository extends JpaRepository<Availability, Long> {

    Availability findFirstByVenue(Venue venue);

}
