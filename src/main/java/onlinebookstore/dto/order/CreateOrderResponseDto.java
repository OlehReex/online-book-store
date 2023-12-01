package onlinebookstore.dto.order;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderResponseDto(
        @NotBlank String shippingAddress) {
}
