package in.vvm.mehfil.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AvailabilityDTO {

    private Long id;

    @NotNull
    @JsonProperty("isAvailable")
    private Boolean isAvailable;

    @NotNull
    private LocalDateTime availableOn;

    @NotNull
    private Long venue;

}
