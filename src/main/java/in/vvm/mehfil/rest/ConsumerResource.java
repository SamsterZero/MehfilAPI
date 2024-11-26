package in.vvm.mehfil.rest;

import in.vvm.mehfil.model.ConsumerDTO;
import in.vvm.mehfil.service.ConsumerService;
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
@RequestMapping(value = "/api/consumers", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConsumerResource {

    private final ConsumerService consumerService;

    public ConsumerResource(final ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @GetMapping
    public ResponseEntity<List<ConsumerDTO>> getAllConsumers() {
        return ResponseEntity.ok(consumerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsumerDTO> getConsumer(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(consumerService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createConsumer(@RequestBody @Valid final ConsumerDTO consumerDTO) {
        final Long createdId = consumerService.create(consumerDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateConsumer(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ConsumerDTO consumerDTO) {
        consumerService.update(id, consumerDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteConsumer(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = consumerService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        consumerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
