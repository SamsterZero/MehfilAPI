package in.vvm.mehfil.service;

import in.vvm.mehfil.domain.Address;
import in.vvm.mehfil.domain.Pincode;
import in.vvm.mehfil.domain.User;
import in.vvm.mehfil.domain.Venue;
import in.vvm.mehfil.model.AddressDTO;
import in.vvm.mehfil.repos.AddressRepository;
import in.vvm.mehfil.repos.PincodeRepository;
import in.vvm.mehfil.repos.UserRepository;
import in.vvm.mehfil.repos.VenueRepository;
import in.vvm.mehfil.util.NotFoundException;
import in.vvm.mehfil.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final PincodeRepository pincodeRepository;
    private final UserRepository userRepository;
    private final VenueRepository venueRepository;

    public AddressService(final AddressRepository addressRepository,
            final PincodeRepository pincodeRepository, final UserRepository userRepository,
            final VenueRepository venueRepository) {
        this.addressRepository = addressRepository;
        this.pincodeRepository = pincodeRepository;
        this.userRepository = userRepository;
        this.venueRepository = venueRepository;
    }

    public List<AddressDTO> findAll() {
        final List<Address> addresses = addressRepository.findAll(Sort.by("id"));
        return addresses.stream()
                .map(address -> mapToDTO(address, new AddressDTO()))
                .toList();
    }

    public AddressDTO get(final Long id) {
        return addressRepository.findById(id)
                .map(address -> mapToDTO(address, new AddressDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AddressDTO addressDTO) {
        final Address address = new Address();
        mapToEntity(addressDTO, address);
        return addressRepository.save(address).getId();
    }

    public void update(final Long id, final AddressDTO addressDTO) {
        final Address address = addressRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(addressDTO, address);
        addressRepository.save(address);
    }

    public void delete(final Long id) {
        addressRepository.deleteById(id);
    }

    private AddressDTO mapToDTO(final Address address, final AddressDTO addressDTO) {
        addressDTO.setId(address.getId());
        addressDTO.setAddressLine1(address.getAddressLine1());
        addressDTO.setAddressLine2(address.getAddressLine2());
        addressDTO.setStreet(address.getStreet());
        addressDTO.setLandmark(address.getLandmark());
        addressDTO.setLocality(address.getLocality());
        addressDTO.setPincode(address.getPincode() == null ? null : address.getPincode().getId());
        return addressDTO;
    }

    private Address mapToEntity(final AddressDTO addressDTO, final Address address) {
        address.setAddressLine1(addressDTO.getAddressLine1());
        address.setAddressLine2(addressDTO.getAddressLine2());
        address.setStreet(addressDTO.getStreet());
        address.setLandmark(addressDTO.getLandmark());
        address.setLocality(addressDTO.getLocality());
        final Pincode pincode = addressDTO.getPincode() == null ? null : pincodeRepository.findById(addressDTO.getPincode())
                .orElseThrow(() -> new NotFoundException("pincode not found"));
        address.setPincode(pincode);
        return address;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Address address = addressRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final User addressUser = userRepository.findFirstByAddress(address);
        if (addressUser != null) {
            referencedWarning.setKey("address.user.address.referenced");
            referencedWarning.addParam(addressUser.getId());
            return referencedWarning;
        }
        final Venue addressVenue = venueRepository.findFirstByAddress(address);
        if (addressVenue != null) {
            referencedWarning.setKey("address.venue.address.referenced");
            referencedWarning.addParam(addressVenue.getId());
            return referencedWarning;
        }
        return null;
    }

}
