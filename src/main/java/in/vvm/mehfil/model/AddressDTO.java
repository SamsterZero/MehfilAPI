package in.vvm.mehfil.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AddressDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String addressLine1;

    @NotNull
    @Size(max = 255)
    private String addressLine2;

    @NotNull
    @Size(max = 255)
    private String street;

    @Size(max = 255)
    private String landmark;

    @NotNull
    @Size(max = 255)
    private String locality;

    @NotNull
    private Long pincode;

}
