package onlinebookstore.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import onlinebookstore.dto.book.BookDto;
import onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import onlinebookstore.dto.book.CreateBookRequestDto;
import onlinebookstore.exception.EntityNotFoundException;
import onlinebookstore.mapper.BookMapper;
import onlinebookstore.mapper.impl.BookMapperImpl;
import onlinebookstore.model.Book;
import onlinebookstore.repository.BookRepository;
import onlinebookstore.service.impl.BookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Spy
    private BookMapper bookMapper = new BookMapperImpl();

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Save valid book")
    void saveBook_validRequest_returnBookDto() {
        Long bookId = 1L;

        Book book = createBook(bookId);

        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto(
                "Clean Code", "Robert Martin", "00-000-000-01",
                new BigDecimal("100.00"), "some description",
                "https://some-image-url.com", List.of());

        Mockito.when(bookMapper.toBook(bookRequestDto)).thenReturn(book);
        Mockito.when(bookRepository.save(book)).thenReturn(book);

        BookDto actual = bookService.save(bookRequestDto);

        assertThat(actual)
                .hasFieldOrPropertyWithValue("title", bookRequestDto.title())
                .hasFieldOrPropertyWithValue("author", bookRequestDto.author())
                .hasFieldOrPropertyWithValue("price", bookRequestDto.price())
                .hasFieldOrPropertyWithValue("description", bookRequestDto.description())
                .hasFieldOrPropertyWithValue("coverImage", bookRequestDto.coverImage())
                .hasFieldOrPropertyWithValue("categoriesIds", bookRequestDto.categoriesIds());
    }

    @Test
    @DisplayName("Find book by valid id")
    void getBook_byValidId_returnBookDto() {
        Long bookId = 1L;

        Book book = createBook(bookId);

        Mockito.when(bookRepository.getBookById(bookId)).thenReturn(Optional.of(book));

        BookDto actual = bookService.findById(bookId);

        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", book.getId())
                .hasFieldOrPropertyWithValue("title", book.getTitle())
                .hasFieldOrPropertyWithValue("author", book.getAuthor())
                .hasFieldOrPropertyWithValue("price", book.getPrice())
                .hasFieldOrPropertyWithValue("description", book.getDescription())
                .hasFieldOrPropertyWithValue("coverImage", book.getCoverImage());
    }

    @Test
    @DisplayName("Find book by incorrect id")
    void getBook_byIncorrectId_throwsException() {
        Long id = 10L;

        Mockito.lenient().when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findById(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Update book")
    void updateBook_validRequest_returnBookDtoWithoutCategoriesIds() {
        Long bookId = 1L;

        Book book = createBook(bookId);

        CreateBookRequestDto updateBookRequestDto = new CreateBookRequestDto(
                "Clean Code", "Robert Martin", "00-000-000-01",
                new BigDecimal("90.00"), "new description",
                "https://some-image-url.com", List.of());

        Book updatedBook = new Book();
        updatedBook.setId(book.getId());
        updatedBook.setTitle(book.getTitle());
        updatedBook.setAuthor(book.getAuthor());
        updatedBook.setIsbn(book.getIsbn());
        updatedBook.setPrice(updateBookRequestDto.price());
        updatedBook.setDescription(updateBookRequestDto.description());
        updatedBook.setCoverImage(book.getCoverImage());
        updatedBook.setCategories(book.getCategories());

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        Mockito.when(bookRepository.save(book)).thenReturn(updatedBook);

        BookDtoWithoutCategoryIds actual = bookService.updateById(bookId, updateBookRequestDto);

        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", updatedBook.getId())
                .hasFieldOrPropertyWithValue("title", updatedBook.getTitle())
                .hasFieldOrPropertyWithValue("author", updatedBook.getAuthor())
                .hasFieldOrPropertyWithValue("price", updatedBook.getPrice())
                .hasFieldOrPropertyWithValue("description", updatedBook.getDescription())
                .hasFieldOrPropertyWithValue("coverImage", updatedBook.getCoverImage());
    }

    @Test
    @DisplayName("Delete book by walid id")
    void deleteBook_byValidId() {
        Long id = 1L;

        Mockito.doNothing().when(bookRepository).deleteById(id);

        bookService.deleteById(id);

        Mockito.verify(bookRepository, Mockito.times(1)).deleteById(id);
        Mockito.verifyNoMoreInteractions(bookRepository);
    }

    private Book createBook(Long bookId) {
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Clean Code");
        book.setAuthor("Robert Martin");
        book.setIsbn("00-000-000-01");
        book.setPrice(new BigDecimal("100.00"));
        book.setDescription("some description");
        book.setCoverImage("https://some-image-url.com");
        book.setCategories(new HashSet<>());
        return book;
    }
}
