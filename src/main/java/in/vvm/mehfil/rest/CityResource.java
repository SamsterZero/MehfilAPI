package in.vvm.mehfil.rest;

import in.vvm.mehfil.model.CityDTO;
import in.vvm.mehfil.service.CityService;
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
@RequestMapping(value = "/api/cities", produces = MediaType.APPLICATION_JSON_VALUE)
public class CityResource {

    private final CityService cityService;

    public CityResource(final CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity<List<CityDTO>> getAllCities() {
        return ResponseEntity.ok(cityService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDTO> getCity(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(cityService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCity(@RequestBody @Valid final CityDTO cityDTO) {
        final Long createdId = cityService.create(cityDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCity(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CityDTO cityDTO) {
        cityService.update(id, cityDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCity(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = cityService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        cityService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
