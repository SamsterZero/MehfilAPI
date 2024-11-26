package in.vvm.mehfil.service;

import in.vvm.mehfil.domain.Country;
import in.vvm.mehfil.domain.PhoneNo;
import in.vvm.mehfil.domain.User;
import in.vvm.mehfil.model.PhoneNoDTO;
import in.vvm.mehfil.repos.CountryRepository;
import in.vvm.mehfil.repos.PhoneNoRepository;
import in.vvm.mehfil.repos.UserRepository;
import in.vvm.mehfil.util.NotFoundException;
import in.vvm.mehfil.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PhoneNoService {

    private final PhoneNoRepository phoneNoRepository;
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;

    public PhoneNoService(final PhoneNoRepository phoneNoRepository,
            final CountryRepository countryRepository, final UserRepository userRepository) {
        this.phoneNoRepository = phoneNoRepository;
        this.countryRepository = countryRepository;
        this.userRepository = userRepository;
    }

    public List<PhoneNoDTO> findAll() {
        final List<PhoneNo> phoneNoes = phoneNoRepository.findAll(Sort.by("id"));
        return phoneNoes.stream()
                .map(phoneNo -> mapToDTO(phoneNo, new PhoneNoDTO()))
                .toList();
    }

    public PhoneNoDTO get(final Long id) {
        return phoneNoRepository.findById(id)
                .map(phoneNo -> mapToDTO(phoneNo, new PhoneNoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PhoneNoDTO phoneNoDTO) {
        final PhoneNo phoneNo = new PhoneNo();
        mapToEntity(phoneNoDTO, phoneNo);
        return phoneNoRepository.save(phoneNo).getId();
    }

    public void update(final Long id, final PhoneNoDTO phoneNoDTO) {
        final PhoneNo phoneNo = phoneNoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(phoneNoDTO, phoneNo);
        phoneNoRepository.save(phoneNo);
    }

    public void delete(final Long id) {
        phoneNoRepository.deleteById(id);
    }

    private PhoneNoDTO mapToDTO(final PhoneNo phoneNo, final PhoneNoDTO phoneNoDTO) {
        phoneNoDTO.setId(phoneNo.getId());
        phoneNoDTO.setPhoneNo(phoneNo.getPhoneNo());
        phoneNoDTO.setCountry(phoneNo.getCountry() == null ? null : phoneNo.getCountry().getId());
        return phoneNoDTO;
    }

    private PhoneNo mapToEntity(final PhoneNoDTO phoneNoDTO, final PhoneNo phoneNo) {
        phoneNo.setPhoneNo(phoneNoDTO.getPhoneNo());
        final Country country = phoneNoDTO.getCountry() == null ? null : countryRepository.findById(phoneNoDTO.getCountry())
                .orElseThrow(() -> new NotFoundException("country not found"));
        phoneNo.setCountry(country);
        return phoneNo;
    }

    public boolean phoneNoExists(final Long phoneNo) {
        return phoneNoRepository.existsByPhoneNo(phoneNo);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final PhoneNo phoneNo = phoneNoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final User phoneNumbesUser = userRepository.findFirstByPhoneNumbes(phoneNo);
        if (phoneNumbesUser != null) {
            referencedWarning.setKey("phoneNo.user.phoneNumbes.referenced");
            referencedWarning.addParam(phoneNumbesUser.getId());
            return referencedWarning;
        }
        return null;
    }

}
