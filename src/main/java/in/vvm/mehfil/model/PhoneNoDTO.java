package in.vvm.mehfil.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PhoneNoDTO {

    private Long id;

    @NotNull
    @PhoneNoPhoneNoUnique
    private Long phoneNo;

    @NotNull
    private Long country;

}
