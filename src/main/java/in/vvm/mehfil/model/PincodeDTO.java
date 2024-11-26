package in.vvm.mehfil.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PincodeDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    @PincodePincodeUnique
    private String pincode;

    @NotNull
    private Long city;

}
