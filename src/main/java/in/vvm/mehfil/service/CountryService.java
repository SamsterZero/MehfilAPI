package in.vvm.mehfil.service;

import in.vvm.mehfil.domain.Country;
import in.vvm.mehfil.domain.PhoneNo;
import in.vvm.mehfil.domain.State;
import in.vvm.mehfil.model.CountryDTO;
import in.vvm.mehfil.repos.CountryRepository;
import in.vvm.mehfil.repos.PhoneNoRepository;
import in.vvm.mehfil.repos.StateRepository;
import in.vvm.mehfil.util.NotFoundException;
import in.vvm.mehfil.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private final StateRepository stateRepository;
    private final PhoneNoRepository phoneNoRepository;

    public CountryService(final CountryRepository countryRepository,
            final StateRepository stateRepository, final PhoneNoRepository phoneNoRepository) {
        this.countryRepository = countryRepository;
        this.stateRepository = stateRepository;
        this.phoneNoRepository = phoneNoRepository;
    }

    public List<CountryDTO> findAll() {
        final List<Country> countries = countryRepository.findAll(Sort.by("id"));
        return countries.stream()
                .map(country -> mapToDTO(country, new CountryDTO()))
                .toList();
    }

    public CountryDTO get(final Long id) {
        return countryRepository.findById(id)
                .map(country -> mapToDTO(country, new CountryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CountryDTO countryDTO) {
        final Country country = new Country();
        mapToEntity(countryDTO, country);
        return countryRepository.save(country).getId();
    }

    public void update(final Long id, final CountryDTO countryDTO) {
        final Country country = countryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(countryDTO, country);
        countryRepository.save(country);
    }

    public void delete(final Long id) {
        countryRepository.deleteById(id);
    }

    private CountryDTO mapToDTO(final Country country, final CountryDTO countryDTO) {
        countryDTO.setId(country.getId());
        countryDTO.setName(country.getName());
        countryDTO.setCode(country.getCode());
        return countryDTO;
    }

    private Country mapToEntity(final CountryDTO countryDTO, final Country country) {
        country.setName(countryDTO.getName());
        country.setCode(countryDTO.getCode());
        return country;
    }

    public boolean nameExists(final String name) {
        return countryRepository.existsByNameIgnoreCase(name);
    }

    public boolean codeExists(final Integer code) {
        return countryRepository.existsByCode(code);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Country country = countryRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final State countryState = stateRepository.findFirstByCountry(country);
        if (countryState != null) {
            referencedWarning.setKey("country.state.country.referenced");
            referencedWarning.addParam(countryState.getId());
            return referencedWarning;
        }
        final PhoneNo countryPhoneNo = phoneNoRepository.findFirstByCountry(country);
        if (countryPhoneNo != null) {
            referencedWarning.setKey("country.phoneNo.country.referenced");
            referencedWarning.addParam(countryPhoneNo.getId());
            return referencedWarning;
        }
        return null;
    }

}
