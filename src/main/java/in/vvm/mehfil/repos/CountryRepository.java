package in.vvm.mehfil.repos;

import in.vvm.mehfil.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CountryRepository extends JpaRepository<Country, Long> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsByCode(Integer code);

}
