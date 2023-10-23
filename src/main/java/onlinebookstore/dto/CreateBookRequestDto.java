package onlinebookstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import onlinebookstore.validation.Isbn;

public record CreateBookRequestDto(
        Long id,
        @NotNull @Size(min = 2) String title,
        @NotNull @Size(min = 2) String author,
        @NotNull @Size(min = 13) @Isbn String isbn,
        @NotNull @Min(0) BigDecimal price,
        String description,
        String coverImage) {
}
