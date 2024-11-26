package in.vvm.mehfil.service;

import in.vvm.mehfil.domain.City;
import in.vvm.mehfil.domain.Country;
import in.vvm.mehfil.domain.State;
import in.vvm.mehfil.model.StateDTO;
import in.vvm.mehfil.repos.CityRepository;
import in.vvm.mehfil.repos.CountryRepository;
import in.vvm.mehfil.repos.StateRepository;
import in.vvm.mehfil.util.NotFoundException;
import in.vvm.mehfil.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class StateService {

    private final StateRepository stateRepository;
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    public StateService(final StateRepository stateRepository,
            final CountryRepository countryRepository, final CityRepository cityRepository) {
        this.stateRepository = stateRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
    }

    public List<StateDTO> findAll() {
        final List<State> states = stateRepository.findAll(Sort.by("id"));
        return states.stream()
                .map(state -> mapToDTO(state, new StateDTO()))
                .toList();
    }

    public StateDTO get(final Long id) {
        return stateRepository.findById(id)
                .map(state -> mapToDTO(state, new StateDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final StateDTO stateDTO) {
        final State state = new State();
        mapToEntity(stateDTO, state);
        return stateRepository.save(state).getId();
    }

    public void update(final Long id, final StateDTO stateDTO) {
        final State state = stateRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(stateDTO, state);
        stateRepository.save(state);
    }

    public void delete(final Long id) {
        stateRepository.deleteById(id);
    }

    private StateDTO mapToDTO(final State state, final StateDTO stateDTO) {
        stateDTO.setId(state.getId());
        stateDTO.setName(state.getName());
        stateDTO.setCountry(state.getCountry() == null ? null : state.getCountry().getId());
        return stateDTO;
    }

    private State mapToEntity(final StateDTO stateDTO, final State state) {
        state.setName(stateDTO.getName());
        final Country country = stateDTO.getCountry() == null ? null : countryRepository.findById(stateDTO.getCountry())
                .orElseThrow(() -> new NotFoundException("country not found"));
        state.setCountry(country);
        return state;
    }

    public boolean nameExists(final String name) {
        return stateRepository.existsByNameIgnoreCase(name);
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final State state = stateRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final City stateCity = cityRepository.findFirstByState(state);
        if (stateCity != null) {
            referencedWarning.setKey("state.city.state.referenced");
            referencedWarning.addParam(stateCity.getId());
            return referencedWarning;
        }
        return null;
    }

}
