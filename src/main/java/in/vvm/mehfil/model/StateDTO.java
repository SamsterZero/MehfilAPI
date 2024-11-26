package in.vvm.mehfil.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class StateDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    @StateNameUnique
    private String name;

    @NotNull
    private Long country;

}
