package in.vvm.mehfil.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    @UserEmailUnique
    private String email;

    @NotNull
    @Size(max = 150)
    private String name;

    @NotNull
    @Size(max = 255)
    private String password;

    @UserConsumerUnique
    private Long consumer;

    @UserVendorUnique
    private Long vendor;

    @NotNull
    private Long phoneNumbes;

    @NotNull
    private Long address;

}
