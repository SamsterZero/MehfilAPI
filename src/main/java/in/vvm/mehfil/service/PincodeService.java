package in.vvm.mehfil.service;

import in.vvm.mehfil.domain.Address;
import in.vvm.mehfil.domain.City;
import in.vvm.mehfil.domain.Pincode;
import in.vvm.mehfil.model.PincodeDTO;
import in.vvm.mehfil.repos.AddressRepository;
import in.vvm.mehfil.repos.CityRepository;
import in.vvm.mehfil.repos.PincodeRepository;
import in.vvm.mehfil.util.NotFoundException;
import in.vvm.mehfil.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PincodeService {

    private final PincodeRepository pincodeRepository;
    private final CityRepository cityRepository;
    private final AddressRepository addressRepository;

    public PincodeService(final PincodeRepository pincodeRepository,
            final CityRepository cityRepository, final AddressRepository addressRepository) {
        this.pincodeRepository = pincodeRepository;
        this.cityRepository = cityRepository;
        this.addressRepository = addressRepository;
    }

    public List<PincodeDTO> findAll() {
        final List<Pincode> pincodes = pincodeRepository.findAll(Sort.by("id"));
        return pincodes.stream()
                .map(pincode -> mapToDTO(pincode, new PincodeDTO()))
                .toList();
    }

    public PincodeDTO get(final Long id) {
        return pincodeRepository.findById(id)
                .map(pincode -> mapToDTO(pincode, new PincodeDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PincodeDTO pincodeDTO) {
        final Pincode pincode = new Pincode();
        mapToEntity(pincodeDTO, pincode);
        return pincodeRepository.save(pincode).getId();
    }

    public void update(final Long id, final PincodeDTO pincodeDTO) {
        final Pincode pincode = pincodeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(pincodeDTO, pincode);
        pincodeRepository.save(pincode);
    }

    public void delete(final Long id) {
        pincodeRepository.deleteById(id);
    }

    private PincodeDTO mapToDTO(final Pincode pincode, final PincodeDTO pincodeDTO) {
        pincodeDTO.setId(pincode.getId());
        pincodeDTO.setPincode(pincode.getPincode());
        pincodeDTO.setCity(pincode.getCity() == null ? null : pincode.getCity().getId());
        return pincodeDTO;
    }

    private Pincode mapToEntity(final PincodeDTO pincodeDTO, final Pincode pincode) {
        pincode.setPincode(pincodeDTO.getPincode());
        final City city = pincodeDTO.getCity() == null ? null : cityRepository.findById(pincodeDTO.getCity())
                .orElseThrow(() -> new NotFoundException("city not found"));
        pincode.setCity(city);
        return pincode;
    }

    public boolean pincodeExists(final String pincode) {
        return pincodeRepository.existsByPincodeIgnoreCase(pincode);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Pincode pincode = pincodeRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Address pincodeAddress = addressRepository.findFirstByPincode(pincode);
        if (pincodeAddress != null) {
            referencedWarning.setKey("pincode.address.pincode.referenced");
            referencedWarning.addParam(pincodeAddress.getId());
            return referencedWarning;
        }
        return null;
    }

}
