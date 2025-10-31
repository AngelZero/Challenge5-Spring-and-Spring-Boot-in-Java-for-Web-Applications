package orders.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/** Payload for order update. */
public record OrderUpdateRequest(
        @Size(max = 255) String notes,
        @NotBlank String status,
        @NotNull @DecimalMin(value = "0.00") BigDecimal total
) {}
