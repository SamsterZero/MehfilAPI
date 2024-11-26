package in.vvm.mehfil.rest;

import in.vvm.mehfil.model.PincodeDTO;
import in.vvm.mehfil.service.PincodeService;
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
@RequestMapping(value = "/api/pincodes", produces = MediaType.APPLICATION_JSON_VALUE)
public class PincodeResource {

    private final PincodeService pincodeService;

    public PincodeResource(final PincodeService pincodeService) {
        this.pincodeService = pincodeService;
    }

    @GetMapping
    public ResponseEntity<List<PincodeDTO>> getAllPincodes() {
        return ResponseEntity.ok(pincodeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PincodeDTO> getPincode(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(pincodeService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPincode(@RequestBody @Valid final PincodeDTO pincodeDTO) {
        final Long createdId = pincodeService.create(pincodeDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePincode(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final PincodeDTO pincodeDTO) {
        pincodeService.update(id, pincodeDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePincode(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = pincodeService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        pincodeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
