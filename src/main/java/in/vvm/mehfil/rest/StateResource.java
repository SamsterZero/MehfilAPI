package in.vvm.mehfil.rest;

import in.vvm.mehfil.model.StateDTO;
import in.vvm.mehfil.service.StateService;
import in.vvm.mehfil.util.ReferencedException;
import in.vvm.mehfil.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/states", produces = MediaType.APPLICATION_JSON_VALUE)
public class StateResource {

    private final StateService stateService;

    public StateResource(final StateService stateService) {
        this.stateService = stateService;
    }

    @GetMapping
    public ResponseEntity<List<StateDTO>> getAllStates() {
        return ResponseEntity.ok(stateService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StateDTO> getState(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(stateService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createState(@RequestBody @Valid final StateDTO stateDTO) {
        final Long createdId = stateService.create(stateDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateState(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final StateDTO stateDTO) {
        stateService.update(id, stateDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteState(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = stateService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        stateService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
