package in.vvm.mehfil.rest;

import in.vvm.mehfil.model.VenueDTO;
import in.vvm.mehfil.service.VenueService;
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
@RequestMapping(value = "/api/venues", produces = MediaType.APPLICATION_JSON_VALUE)
public class VenueResource {

    private final VenueService venueService;

    public VenueResource(final VenueService venueService) {
        this.venueService = venueService;
    }

    @GetMapping
    public ResponseEntity<List<VenueDTO>> getAllVenues() {
        return ResponseEntity.ok(venueService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueDTO> getVenue(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(venueService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createVenue(@RequestBody @Valid final VenueDTO venueDTO) {
        final Long createdId = venueService.create(venueDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateVenue(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final VenueDTO venueDTO) {
        venueService.update(id, venueDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteVenue(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = venueService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        venueService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
