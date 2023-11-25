package onlinebookstore.dto.shopping.cart;

public record CartItemResponseDto(
        Long id,
        Long bookId,
        Integer quantity,
        String bookTitle
) {
}
