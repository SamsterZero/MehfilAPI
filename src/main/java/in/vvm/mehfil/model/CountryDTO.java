package in.vvm.mehfil.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CountryDTO {

    private Long id;

    @NotNull
    @Size(max = 100)
    @CountryNameUnique
    private String name;

    @NotNull
    @CountryCodeUnique
    private Integer code;

}
