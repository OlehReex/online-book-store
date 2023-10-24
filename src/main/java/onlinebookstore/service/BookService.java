package onlinebookstore.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import onlinebookstore.dto.BookDto;
import onlinebookstore.dto.CreateBookRequestDto;

public interface BookService {

    BookDto save(CreateBookRequestDto bookRequestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto updateBookRequestDto);
}
