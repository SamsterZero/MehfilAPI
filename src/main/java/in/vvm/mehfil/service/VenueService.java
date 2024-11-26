package in.vvm.mehfil.service;

import in.vvm.mehfil.domain.Address;
import in.vvm.mehfil.domain.Availability;
import in.vvm.mehfil.domain.Product;
import in.vvm.mehfil.domain.Venue;
import in.vvm.mehfil.model.VenueDTO;
import in.vvm.mehfil.repos.AddressRepository;
import in.vvm.mehfil.repos.AvailabilityRepository;
import in.vvm.mehfil.repos.ProductRepository;
import in.vvm.mehfil.repos.VenueRepository;
import in.vvm.mehfil.util.NotFoundException;
import in.vvm.mehfil.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class VenueService {

    private final VenueRepository venueRepository;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final AvailabilityRepository availabilityRepository;

    public VenueService(final VenueRepository venueRepository,
            final AddressRepository addressRepository, final ProductRepository productRepository,
            final AvailabilityRepository availabilityRepository) {
        this.venueRepository = venueRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
        this.availabilityRepository = availabilityRepository;
    }

    public List<VenueDTO> findAll() {
        final List<Venue> venues = venueRepository.findAll(Sort.by("id"));
        return venues.stream()
                .map(venue -> mapToDTO(venue, new VenueDTO()))
                .toList();
    }

    public VenueDTO get(final Long id) {
        return venueRepository.findById(id)
                .map(venue -> mapToDTO(venue, new VenueDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final VenueDTO venueDTO) {
        final Venue venue = new Venue();
        mapToEntity(venueDTO, venue);
        return venueRepository.save(venue).getId();
    }

    public void update(final Long id, final VenueDTO venueDTO) {
        final Venue venue = venueRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(venueDTO, venue);
        venueRepository.save(venue);
    }

    public void delete(final Long id) {
        venueRepository.deleteById(id);
    }

    private VenueDTO mapToDTO(final Venue venue, final VenueDTO venueDTO) {
        venueDTO.setId(venue.getId());
        venueDTO.setName(venue.getName());
        venueDTO.setArea(venue.getArea());
        venueDTO.setAreaUnits(venue.getAreaUnits());
        venueDTO.setType(venue.getType());
        venueDTO.setAddress(venue.getAddress() == null ? null : venue.getAddress().getId());
        return venueDTO;
    }

    private Venue mapToEntity(final VenueDTO venueDTO, final Venue venue) {
        venue.setName(venueDTO.getName());
        venue.setArea(venueDTO.getArea());
        venue.setAreaUnits(venueDTO.getAreaUnits());
        venue.setType(venueDTO.getType());
        final Address address = venueDTO.getAddress() == null ? null : addressRepository.findById(venueDTO.getAddress())
                .orElseThrow(() -> new NotFoundException("address not found"));
        venue.setAddress(address);
        return venue;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Venue venue = venueRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Product venueProduct = productRepository.findFirstByVenue(venue);
        if (venueProduct != null) {
            referencedWarning.setKey("venue.product.venue.referenced");
            referencedWarning.addParam(venueProduct.getId());
            return referencedWarning;
        }
        final Availability venueAvailability = availabilityRepository.findFirstByVenue(venue);
        if (venueAvailability != null) {
            referencedWarning.setKey("venue.availability.venue.referenced");
            referencedWarning.addParam(venueAvailability.getId());
            return referencedWarning;
        }
        return null;
    }

}
