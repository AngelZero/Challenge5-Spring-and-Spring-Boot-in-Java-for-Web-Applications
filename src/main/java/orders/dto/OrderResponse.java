package orders.dto;

import java.math.BigDecimal;

/** API response. */
public record OrderResponse(
        Long id,
        String notes,
        String status,
        BigDecimal total
        //Instant createdAt
) {}
