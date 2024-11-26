package in.vvm.mehfil.repos;

import in.vvm.mehfil.domain.City;
import in.vvm.mehfil.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CityRepository extends JpaRepository<City, Long> {

    City findFirstByState(State state);

    boolean existsByNameIgnoreCase(String name);

}
