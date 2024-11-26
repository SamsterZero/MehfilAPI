package in.vvm.mehfil.model;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderDTO {

    private Long id;

    @NotNull
    private OrderStatus status;

    @NotNull
    private Long consumer;

    private List<Long> products;

}
