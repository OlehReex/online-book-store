package onlinebookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import onlinebookstore.validation.isbn.Isbn;

public record CreateBookRequestDto(
        Long id,
        @NotBlank String title,
        @NotBlank String author,
        @NotBlank @Isbn String isbn,
        @NotNull @PositiveOrZero BigDecimal price,
        String description,
        String coverImage) {
}
