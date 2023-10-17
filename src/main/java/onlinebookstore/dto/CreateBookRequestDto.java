package onlinebookstore.dto;

import java.math.BigDecimal;

public record CreateBookRequestDto(
        Long id, String title, String author, String isbn,
        BigDecimal price, String description, String coverImage) {
}
