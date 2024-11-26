package in.vvm.mehfil.service;

import in.vvm.mehfil.domain.Consumer;
import in.vvm.mehfil.domain.Order;
import in.vvm.mehfil.domain.Product;
import in.vvm.mehfil.model.OrderDTO;
import in.vvm.mehfil.repos.ConsumerRepository;
import in.vvm.mehfil.repos.OrderRepository;
import in.vvm.mehfil.repos.ProductRepository;
import in.vvm.mehfil.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ConsumerRepository consumerRepository;
    private final ProductRepository productRepository;

    public OrderService(final OrderRepository orderRepository,
            final ConsumerRepository consumerRepository,
            final ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.consumerRepository = consumerRepository;
        this.productRepository = productRepository;
    }

    public List<OrderDTO> findAll() {
        final List<Order> orders = orderRepository.findAll(Sort.by("id"));
        return orders.stream()
                .map(order -> mapToDTO(order, new OrderDTO()))
                .toList();
    }

    public OrderDTO get(final Long id) {
        return orderRepository.findById(id)
                .map(order -> mapToDTO(order, new OrderDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final OrderDTO orderDTO) {
        final Order order = new Order();
        mapToEntity(orderDTO, order);
        return orderRepository.save(order).getId();
    }

    public void update(final Long id, final OrderDTO orderDTO) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(orderDTO, order);
        orderRepository.save(order);
    }

    public void delete(final Long id) {
        orderRepository.deleteById(id);
    }

    private OrderDTO mapToDTO(final Order order, final OrderDTO orderDTO) {
        orderDTO.setId(order.getId());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setConsumer(order.getConsumer() == null ? null : order.getConsumer().getId());
        orderDTO.setProducts(order.getProducts().stream()
                .map(product -> product.getId())
                .toList());
        return orderDTO;
    }

    private Order mapToEntity(final OrderDTO orderDTO, final Order order) {
        order.setStatus(orderDTO.getStatus());
        final Consumer consumer = orderDTO.getConsumer() == null ? null : consumerRepository.findById(orderDTO.getConsumer())
                .orElseThrow(() -> new NotFoundException("consumer not found"));
        order.setConsumer(consumer);
        final List<Product> products = productRepository.findAllById(
                orderDTO.getProducts() == null ? Collections.emptyList() : orderDTO.getProducts());
        if (products.size() != (orderDTO.getProducts() == null ? 0 : orderDTO.getProducts().size())) {
            throw new NotFoundException("one of products not found");
        }
        order.setProducts(new HashSet<>(products));
        return order;
    }

}
