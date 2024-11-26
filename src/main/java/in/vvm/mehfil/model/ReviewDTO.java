package in.vvm.mehfil.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReviewDTO {

    private Long id;

    private Rating rating;

    private Long user;

    @NotNull
    private Long product;

}
