package in.vvm.mehfil.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ProductDTO {

    private Long id;

    @NotNull
    private Category category;

    @Size(max = 255)
    private String description;

    @ProductVenueUnique
    private Long venue;

    @NotNull
    private Long vendor;

}
