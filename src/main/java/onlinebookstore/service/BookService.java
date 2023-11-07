package onlinebookstore.service;

import java.util.List;
import onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import onlinebookstore.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {

    BookDtoWithoutCategoryIds save(CreateBookRequestDto bookRequestDto);

    List<BookDtoWithoutCategoryIds> findAll(Pageable pageable);

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long id, Pageable pageable);

    BookDtoWithoutCategoryIds findById(Long id);

    void deleteById(Long id);

    BookDtoWithoutCategoryIds updateById(Long id, CreateBookRequestDto updateBookRequestDto);
}
