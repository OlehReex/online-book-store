package onlinebookstore.dto.order;

public record OrderItemResponseDto(
        Long id,
        Long bookId,
        Integer quantity) {
}

