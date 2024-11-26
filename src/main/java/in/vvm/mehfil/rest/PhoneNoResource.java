package in.vvm.mehfil.rest;

import in.vvm.mehfil.model.PhoneNoDTO;
import in.vvm.mehfil.service.PhoneNoService;
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
@RequestMapping(value = "/api/phoneNos", produces = MediaType.APPLICATION_JSON_VALUE)
public class PhoneNoResource {

    private final PhoneNoService phoneNoService;

    public PhoneNoResource(final PhoneNoService phoneNoService) {
        this.phoneNoService = phoneNoService;
    }

    @GetMapping
    public ResponseEntity<List<PhoneNoDTO>> getAllPhoneNos() {
        return ResponseEntity.ok(phoneNoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhoneNoDTO> getPhoneNo(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(phoneNoService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPhoneNo(@RequestBody @Valid final PhoneNoDTO phoneNoDTO) {
        final Long createdId = phoneNoService.create(phoneNoDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePhoneNo(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final PhoneNoDTO phoneNoDTO) {
        phoneNoService.update(id, phoneNoDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePhoneNo(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = phoneNoService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        phoneNoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
