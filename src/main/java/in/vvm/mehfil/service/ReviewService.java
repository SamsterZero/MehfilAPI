package in.vvm.mehfil.service;

import in.vvm.mehfil.domain.Consumer;
import in.vvm.mehfil.domain.Product;
import in.vvm.mehfil.domain.Review;
import in.vvm.mehfil.model.ReviewDTO;
import in.vvm.mehfil.repos.ConsumerRepository;
import in.vvm.mehfil.repos.ProductRepository;
import in.vvm.mehfil.repos.ReviewRepository;
import in.vvm.mehfil.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ConsumerRepository consumerRepository;
    private final ProductRepository productRepository;

    public ReviewService(final ReviewRepository reviewRepository,
            final ConsumerRepository consumerRepository,
            final ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.consumerRepository = consumerRepository;
        this.productRepository = productRepository;
    }

    public List<ReviewDTO> findAll() {
        final List<Review> reviews = reviewRepository.findAll(Sort.by("id"));
        return reviews.stream()
                .map(review -> mapToDTO(review, new ReviewDTO()))
                .toList();
    }

    public ReviewDTO get(final Long id) {
        return reviewRepository.findById(id)
                .map(review -> mapToDTO(review, new ReviewDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ReviewDTO reviewDTO) {
        final Review review = new Review();
        mapToEntity(reviewDTO, review);
        return reviewRepository.save(review).getId();
    }

    public void update(final Long id, final ReviewDTO reviewDTO) {
        final Review review = reviewRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reviewDTO, review);
        reviewRepository.save(review);
    }

    public void delete(final Long id) {
        reviewRepository.deleteById(id);
    }

    private ReviewDTO mapToDTO(final Review review, final ReviewDTO reviewDTO) {
        reviewDTO.setId(review.getId());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setUser(review.getUser() == null ? null : review.getUser().getId());
        reviewDTO.setProduct(review.getProduct() == null ? null : review.getProduct().getId());
        return reviewDTO;
    }

    private Review mapToEntity(final ReviewDTO reviewDTO, final Review review) {
        review.setRating(reviewDTO.getRating());
        final Consumer user = reviewDTO.getUser() == null ? null : consumerRepository.findById(reviewDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        review.setUser(user);
        final Product product = reviewDTO.getProduct() == null ? null : productRepository.findById(reviewDTO.getProduct())
                .orElseThrow(() -> new NotFoundException("product not found"));
        review.setProduct(product);
        return review;
    }

}
