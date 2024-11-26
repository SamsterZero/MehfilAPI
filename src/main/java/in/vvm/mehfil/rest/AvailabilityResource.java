package in.vvm.mehfil.rest;

import in.vvm.mehfil.model.AvailabilityDTO;
import in.vvm.mehfil.service.AvailabilityService;
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
@RequestMapping(value = "/api/availabilities", produces = MediaType.APPLICATION_JSON_VALUE)
public class AvailabilityResource {

    private final AvailabilityService availabilityService;

    public AvailabilityResource(final AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping
    public ResponseEntity<List<AvailabilityDTO>> getAllAvailabilities() {
        return ResponseEntity.ok(availabilityService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvailabilityDTO> getAvailability(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(availabilityService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAvailability(
            @RequestBody @Valid final AvailabilityDTO availabilityDTO) {
        final Long createdId = availabilityService.create(availabilityDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateAvailability(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final AvailabilityDTO availabilityDTO) {
        availabilityService.update(id, availabilityDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAvailability(@PathVariable(name = "id") final Long id) {
        availabilityService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
