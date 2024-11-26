package in.vvm.mehfil.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VendorDTO {

    private Long id;

    @NotNull
    @Size(max = 150)
    private String companyName;

    @NotNull
    @Size(max = 150)
    private String contactName;

}
