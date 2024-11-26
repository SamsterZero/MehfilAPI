package in.vvm.mehfil.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ConsumerDTO {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String firstName;

    @NotNull
    @Size(max = 100)
    private String lastName;

    private List<Long> favourites;

}
