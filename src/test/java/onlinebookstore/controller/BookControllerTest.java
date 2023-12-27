package onlinebookstore.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.context.jdbc.SqlMergeMode.MergeMode.MERGE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import onlinebookstore.dto.book.BookDto;
import onlinebookstore.dto.book.CreateBookRequestDto;
import onlinebookstore.exception.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SqlMergeMode(MERGE)
@Sql(scripts = {"classpath:database/add-categories-to-database.sql",
        "classpath:database/add-books-to-database.sql",
        "classpath:database/add-books-categories-dependencies.sql"})
@Sql(scripts = {"classpath:database/delete-all-from-database.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Create book with valid request")
    @WithMockUser(username = "admin@test.com", roles = {"USER", "ADMIN"})
    void createBook_validRequestDto_returnsDto() throws Exception {
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto(
                "Effective Java", "Joshua Bloch", "00-000-000-30",
                new BigDecimal("110.00"), "some description",
                "https://some-image-url.com", List.of(1L));

        MvcResult mvcResult = mockMvc.perform(post("/books")
                        .content(objectMapper.writeValueAsString(bookRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), BookDto.class);

        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", 3L)
                .hasFieldOrPropertyWithValue("author", bookRequestDto.author())
                .hasFieldOrPropertyWithValue("price", bookRequestDto.price())
                .hasFieldOrPropertyWithValue("description", bookRequestDto.description())
                .hasFieldOrPropertyWithValue("coverImage", bookRequestDto.coverImage())
                .hasFieldOrPropertyWithValue("categoriesIds", Collections.singletonList(1L));
    }

    @Test
    @DisplayName("Get book by valid id")
    @WithMockUser(username = "admin@test.com", roles = {"USER", "ADMIN"})
    void getBook_byValidId_returnsDto() throws Exception {
        Long bookId = 1L;

        BookDto bookDto = new BookDto();
        bookDto.setId(bookId);
        bookDto.setTitle("Clean Code");
        bookDto.setAuthor("Robert Martin");
        bookDto.setIsbn("00-000-000-01");
        bookDto.setPrice(BigDecimal.valueOf(100.45));

        MvcResult mvcResult = mockMvc.perform(get("/books/" + bookId))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), BookDto.class);

        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("id", bookId)
                .hasFieldOrPropertyWithValue("title", bookDto.getTitle())
                .hasFieldOrPropertyWithValue("author", bookDto.getAuthor())
                .hasFieldOrPropertyWithValue("isbn", bookDto.getIsbn())
                .hasFieldOrPropertyWithValue("price", bookDto.getPrice());
    }

    @Test
    @DisplayName("Get book by incorrect id")
    @WithMockUser(username = "admin@test.com", roles = {"USER", "ADMIN"})
    void getBook_byIncorrectId_throwsException() throws Exception {
        Long bookId = 10L;

        MvcResult mvcResult = mockMvc.perform(get("/books/" + bookId))
                .andExpect(status().isNotFound())
                .andReturn();

        Exception resolvedException = mvcResult.getResolvedException();
        Assertions.assertNotNull(resolvedException);
        Assertions.assertTrue(EntityNotFoundException.class
                .isAssignableFrom(resolvedException.getClass()));
    }

    @Test
    @DisplayName("Delete book by valid id")
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void deleteBook_byValidId() throws Exception {
        Long bookId = 1L;

        mockMvc.perform(delete("/books/" + bookId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }
}
