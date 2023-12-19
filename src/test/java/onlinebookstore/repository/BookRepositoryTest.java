package onlinebookstore.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;
import onlinebookstore.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = {"classpath:database/add-categories-to-database.sql",
        "classpath:database/add-books-to-database.sql",
        "classpath:database/add-books-categories-dependencies.sql"})
@Sql(scripts = {"classpath:database/delete-all-from-database.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find all books by category id")
    void findBooks_byValidCategoryId_returnBooksList() {
        Long categoryId = 1L;
        List<Book> actual = bookRepository.findAllByCategoryId(categoryId, PageRequest.of(0, 10));
        assertThat(actual).hasSize(2)
                .element(0)
                .hasFieldOrPropertyWithValue("title", "Clean Code");
    }

    @Test
    @DisplayName("Find all books by category id")
    void findBooks_byIncorrectCategoryId_returnEmptyList() {
        Long categoryId = 10L;
        List<Book> actual = bookRepository.findAllByCategoryId(categoryId, PageRequest.of(0, 10));
        assertThat(actual).isEmpty();
    }
}
