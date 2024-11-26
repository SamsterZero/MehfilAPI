package in.vvm.mehfil.service;

import in.vvm.mehfil.domain.Consumer;
import in.vvm.mehfil.domain.Order;
import in.vvm.mehfil.domain.Product;
import in.vvm.mehfil.domain.Review;
import in.vvm.mehfil.domain.User;
import in.vvm.mehfil.model.ConsumerDTO;
import in.vvm.mehfil.repos.ConsumerRepository;
import in.vvm.mehfil.repos.OrderRepository;
import in.vvm.mehfil.repos.ProductRepository;
import in.vvm.mehfil.repos.ReviewRepository;
import in.vvm.mehfil.repos.UserRepository;
import in.vvm.mehfil.util.NotFoundException;
import in.vvm.mehfil.util.ReferencedWarning;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ConsumerService {

    private final ConsumerRepository consumerRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;

    public ConsumerService(final ConsumerRepository consumerRepository,
            final ProductRepository productRepository, final UserRepository userRepository,
            final ReviewRepository reviewRepository, final OrderRepository orderRepository) {
        this.consumerRepository = consumerRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.orderRepository = orderRepository;
    }

    public List<ConsumerDTO> findAll() {
        final List<Consumer> consumers = consumerRepository.findAll(Sort.by("id"));
        return consumers.stream()
                .map(consumer -> mapToDTO(consumer, new ConsumerDTO()))
                .toList();
    }

    public ConsumerDTO get(final Long id) {
        return consumerRepository.findById(id)
                .map(consumer -> mapToDTO(consumer, new ConsumerDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ConsumerDTO consumerDTO) {
        final Consumer consumer = new Consumer();
        mapToEntity(consumerDTO, consumer);
        return consumerRepository.save(consumer).getId();
    }

    public void update(final Long id, final ConsumerDTO consumerDTO) {
        final Consumer consumer = consumerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(consumerDTO, consumer);
        consumerRepository.save(consumer);
    }

    public void delete(final Long id) {
        consumerRepository.deleteById(id);
    }

    private ConsumerDTO mapToDTO(final Consumer consumer, final ConsumerDTO consumerDTO) {
        consumerDTO.setId(consumer.getId());
        consumerDTO.setFirstName(consumer.getFirstName());
        consumerDTO.setLastName(consumer.getLastName());
        consumerDTO.setFavourites(consumer.getFavourites().stream()
                .map(product -> product.getId())
                .toList());
        return consumerDTO;
    }

    private Consumer mapToEntity(final ConsumerDTO consumerDTO, final Consumer consumer) {
        consumer.setFirstName(consumerDTO.getFirstName());
        consumer.setLastName(consumerDTO.getLastName());
        final List<Product> favourites = productRepository.findAllById(
                consumerDTO.getFavourites() == null ? Collections.emptyList() : consumerDTO.getFavourites());
        if (favourites.size() != (consumerDTO.getFavourites() == null ? 0 : consumerDTO.getFavourites().size())) {
            throw new NotFoundException("one of favourites not found");
        }
        consumer.setFavourites(new HashSet<>(favourites));
        return consumer;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Consumer consumer = consumerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final User consumerUser = userRepository.findFirstByConsumer(consumer);
        if (consumerUser != null) {
            referencedWarning.setKey("consumer.user.consumer.referenced");
            referencedWarning.addParam(consumerUser.getId());
            return referencedWarning;
        }
        final Review userReview = reviewRepository.findFirstByUser(consumer);
        if (userReview != null) {
            referencedWarning.setKey("consumer.review.user.referenced");
            referencedWarning.addParam(userReview.getId());
            return referencedWarning;
        }
        final Order consumerOrder = orderRepository.findFirstByConsumer(consumer);
        if (consumerOrder != null) {
            referencedWarning.setKey("consumer.order.consumer.referenced");
            referencedWarning.addParam(consumerOrder.getId());
            return referencedWarning;
        }
        return null;
    }

}
