package onlinebookstore.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;
import onlinebookstore.validation.isbn.Isbn;

public record CreateBookRequestDto(
        Long id,
        @NotBlank String title,
        @NotBlank String author,
        @NotBlank @Isbn String isbn,
        @NotNull @PositiveOrZero BigDecimal price,
        List<Long> categoriesId,
        String description,
        String coverImage) {
}
