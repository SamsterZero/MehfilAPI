package in.vvm.mehfil.repos;

import in.vvm.mehfil.domain.Country;
import in.vvm.mehfil.domain.State;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StateRepository extends JpaRepository<State, Long> {

    State findFirstByCountry(Country country);

    boolean existsByNameIgnoreCase(String name);

}
