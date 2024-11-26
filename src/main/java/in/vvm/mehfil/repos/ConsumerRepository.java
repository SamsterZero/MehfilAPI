package in.vvm.mehfil.repos;

import in.vvm.mehfil.domain.Consumer;
import in.vvm.mehfil.domain.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

    Consumer findFirstByFavourites(Product product);

    List<Consumer> findAllByFavourites(Product product);

}
