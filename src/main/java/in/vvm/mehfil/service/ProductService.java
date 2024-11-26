package in.vvm.mehfil.service;

import in.vvm.mehfil.domain.Product;
import in.vvm.mehfil.domain.Review;
import in.vvm.mehfil.domain.Vendor;
import in.vvm.mehfil.domain.Venue;
import in.vvm.mehfil.model.ProductDTO;
import in.vvm.mehfil.repos.ConsumerRepository;
import in.vvm.mehfil.repos.OrderRepository;
import in.vvm.mehfil.repos.ProductRepository;
import in.vvm.mehfil.repos.ReviewRepository;
import in.vvm.mehfil.repos.VendorRepository;
import in.vvm.mehfil.repos.VenueRepository;
import in.vvm.mehfil.util.NotFoundException;
import in.vvm.mehfil.util.ReferencedWarning;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final VenueRepository venueRepository;
    private final VendorRepository vendorRepository;
    private final ConsumerRepository consumerRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    public ProductService(final ProductRepository productRepository,
            final VenueRepository venueRepository, final VendorRepository vendorRepository,
            final ConsumerRepository consumerRepository, final OrderRepository orderRepository,
            final ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.venueRepository = venueRepository;
        this.vendorRepository = vendorRepository;
        this.consumerRepository = consumerRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<ProductDTO> findAll() {
        final List<Product> products = productRepository.findAll(Sort.by("id"));
        return products.stream()
                .map(product -> mapToDTO(product, new ProductDTO()))
                .toList();
    }

    public ProductDTO get(final Long id) {
        return productRepository.findById(id)
                .map(product -> mapToDTO(product, new ProductDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ProductDTO productDTO) {
        final Product product = new Product();
        mapToEntity(productDTO, product);
        return productRepository.save(product).getId();
    }

    public void update(final Long id, final ProductDTO productDTO) {
        final Product product = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(productDTO, product);
        productRepository.save(product);
    }

    public void delete(final Long id) {
        final Product product = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        consumerRepository.findAllByFavourites(product)
                .forEach(consumer -> consumer.getFavourites().remove(product));
        orderRepository.findAllByProducts(product)
                .forEach(order -> order.getProducts().remove(product));
        productRepository.delete(product);
    }

    private ProductDTO mapToDTO(final Product product, final ProductDTO productDTO) {
        productDTO.setId(product.getId());
        productDTO.setCategory(product.getCategory());
        productDTO.setDescription(product.getDescription());
        productDTO.setVenue(product.getVenue() == null ? null : product.getVenue().getId());
        productDTO.setVendor(product.getVendor() == null ? null : product.getVendor().getId());
        return productDTO;
    }

    private Product mapToEntity(final ProductDTO productDTO, final Product product) {
        product.setCategory(productDTO.getCategory());
        product.setDescription(productDTO.getDescription());
        final Venue venue = productDTO.getVenue() == null ? null : venueRepository.findById(productDTO.getVenue())
                .orElseThrow(() -> new NotFoundException("venue not found"));
        product.setVenue(venue);
        final Vendor vendor = productDTO.getVendor() == null ? null : vendorRepository.findById(productDTO.getVendor())
                .orElseThrow(() -> new NotFoundException("vendor not found"));
        product.setVendor(vendor);
        return product;
    }

    public boolean venueExists(final Long id) {
        return productRepository.existsByVenueId(id);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Product product = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Review productReview = reviewRepository.findFirstByProduct(product);
        if (productReview != null) {
            referencedWarning.setKey("product.review.product.referenced");
            referencedWarning.addParam(productReview.getId());
            return referencedWarning;
        }
        return null;
    }

}
