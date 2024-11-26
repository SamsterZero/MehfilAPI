package in.vvm.mehfil.service;

import in.vvm.mehfil.domain.City;
import in.vvm.mehfil.domain.Pincode;
import in.vvm.mehfil.domain.State;
import in.vvm.mehfil.model.CityDTO;
import in.vvm.mehfil.repos.CityRepository;
import in.vvm.mehfil.repos.PincodeRepository;
import in.vvm.mehfil.repos.StateRepository;
import in.vvm.mehfil.util.NotFoundException;
import in.vvm.mehfil.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CityService {

    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final PincodeRepository pincodeRepository;

    public CityService(final CityRepository cityRepository, final StateRepository stateRepository,
            final PincodeRepository pincodeRepository) {
        this.cityRepository = cityRepository;
        this.stateRepository = stateRepository;
        this.pincodeRepository = pincodeRepository;
    }

    public List<CityDTO> findAll() {
        final List<City> cities = cityRepository.findAll(Sort.by("id"));
        return cities.stream()
                .map(city -> mapToDTO(city, new CityDTO()))
                .toList();
    }

    public CityDTO get(final Long id) {
        return cityRepository.findById(id)
                .map(city -> mapToDTO(city, new CityDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CityDTO cityDTO) {
        final City city = new City();
        mapToEntity(cityDTO, city);
        return cityRepository.save(city).getId();
    }

    public void update(final Long id, final CityDTO cityDTO) {
        final City city = cityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(cityDTO, city);
        cityRepository.save(city);
    }

    public void delete(final Long id) {
        cityRepository.deleteById(id);
    }

    private CityDTO mapToDTO(final City city, final CityDTO cityDTO) {
        cityDTO.setId(city.getId());
        cityDTO.setName(city.getName());
        cityDTO.setState(city.getState() == null ? null : city.getState().getId());
        return cityDTO;
    }

    private City mapToEntity(final CityDTO cityDTO, final City city) {
        city.setName(cityDTO.getName());
        final State state = cityDTO.getState() == null ? null : stateRepository.findById(cityDTO.getState())
                .orElseThrow(() -> new NotFoundException("state not found"));
        city.setState(state);
        return city;
    }

    public boolean nameExists(final String name) {
        return cityRepository.existsByNameIgnoreCase(name);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final City city = cityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Pincode cityPincode = pincodeRepository.findFirstByCity(city);
        if (cityPincode != null) {
            referencedWarning.setKey("city.pincode.city.referenced");
            referencedWarning.addParam(cityPincode.getId());
            return referencedWarning;
        }
        return null;
    }

}
