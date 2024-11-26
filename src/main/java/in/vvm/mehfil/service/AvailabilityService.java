package in.vvm.mehfil.service;

import in.vvm.mehfil.domain.Availability;
import in.vvm.mehfil.domain.Venue;
import in.vvm.mehfil.model.AvailabilityDTO;
import in.vvm.mehfil.repos.AvailabilityRepository;
import in.vvm.mehfil.repos.VenueRepository;
import in.vvm.mehfil.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final VenueRepository venueRepository;

    public AvailabilityService(final AvailabilityRepository availabilityRepository,
            final VenueRepository venueRepository) {
        this.availabilityRepository = availabilityRepository;
        this.venueRepository = venueRepository;
    }

    public List<AvailabilityDTO> findAll() {
        final List<Availability> availabilities = availabilityRepository.findAll(Sort.by("id"));
        return availabilities.stream()
                .map(availability -> mapToDTO(availability, new AvailabilityDTO()))
                .toList();
    }

    public AvailabilityDTO get(final Long id) {
        return availabilityRepository.findById(id)
                .map(availability -> mapToDTO(availability, new AvailabilityDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AvailabilityDTO availabilityDTO) {
        final Availability availability = new Availability();
        mapToEntity(availabilityDTO, availability);
        return availabilityRepository.save(availability).getId();
    }

    public void update(final Long id, final AvailabilityDTO availabilityDTO) {
        final Availability availability = availabilityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(availabilityDTO, availability);
        availabilityRepository.save(availability);
    }

    public void delete(final Long id) {
        availabilityRepository.deleteById(id);
    }

    private AvailabilityDTO mapToDTO(final Availability availability,
            final AvailabilityDTO availabilityDTO) {
        availabilityDTO.setId(availability.getId());
        availabilityDTO.setIsAvailable(availability.getIsAvailable());
        availabilityDTO.setAvailableOn(availability.getAvailableOn());
        availabilityDTO.setVenue(availability.getVenue() == null ? null : availability.getVenue().getId());
        return availabilityDTO;
    }

    private Availability mapToEntity(final AvailabilityDTO availabilityDTO,
            final Availability availability) {
        availability.setIsAvailable(availabilityDTO.getIsAvailable());
        availability.setAvailableOn(availabilityDTO.getAvailableOn());
        final Venue venue = availabilityDTO.getVenue() == null ? null : venueRepository.findById(availabilityDTO.getVenue())
                .orElseThrow(() -> new NotFoundException("venue not found"));
        availability.setVenue(venue);
        return availability;
    }

}
