package in.vvm.mehfil.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VenueDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    private Integer area;

    @NotNull
    @Size(max = 50)
    private String areaUnits;

    @NotNull
    private VenueType type;

    private Long address;

}
