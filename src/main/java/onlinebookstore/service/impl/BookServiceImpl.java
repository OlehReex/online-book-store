package onlinebookstore.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.book.BookDto;
import onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import onlinebookstore.dto.book.CreateBookRequestDto;
import onlinebookstore.exception.EntityNotFoundException;
import onlinebookstore.mapper.BookMapper;
import onlinebookstore.model.Book;
import onlinebookstore.repository.BookRepository;
import onlinebookstore.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDtoWithoutCategoryIds save(CreateBookRequestDto bookRequestDto) {
        Book savedBook = bookRepository
                .save(bookMapper.toBook(bookRequestDto));
        return bookMapper.toBookWithoutCategories(savedBook);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .stream()
                .map(bookMapper::toBookWithoutCategories)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long id, Pageable pageable) {
        return bookRepository.findAllByCategoryId(id, pageable).stream()
                .map(bookMapper::toBookWithoutCategories)
                .toList();
    }

    @Override
    public BookDtoWithoutCategoryIds findById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toBookWithoutCategories)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book with id " + id));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public BookDtoWithoutCategoryIds updateById(
            Long id, CreateBookRequestDto updateBookRequestDto) {
        Book bookToUpdate = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book with id " + id));
        bookMapper.updateBook(updateBookRequestDto, bookToUpdate);
        return bookMapper.toBookWithoutCategories(bookRepository.save(bookToUpdate));
    }
}
