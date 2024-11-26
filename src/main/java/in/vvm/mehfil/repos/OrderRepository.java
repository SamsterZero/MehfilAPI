package in.vvm.mehfil.repos;

import in.vvm.mehfil.domain.Consumer;
import in.vvm.mehfil.domain.Order;
import in.vvm.mehfil.domain.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findFirstByConsumer(Consumer consumer);

    Order findFirstByProducts(Product product);

    List<Order> findAllByProducts(Product product);

}
