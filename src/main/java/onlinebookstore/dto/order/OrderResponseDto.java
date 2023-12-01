package onlinebookstore.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import onlinebookstore.model.Status;

public record OrderResponseDto(
        Long id,
        Long userId,
        LocalDateTime orderDate,
        BigDecimal total,
        Status status,
        List<OrderItemResponseDto> orderItems) {
}
