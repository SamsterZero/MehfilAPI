package in.vvm.mehfil.repos;

import in.vvm.mehfil.domain.Consumer;
import in.vvm.mehfil.domain.Product;
import in.vvm.mehfil.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findFirstByUser(Consumer consumer);

    Review findFirstByProduct(Product product);

}
