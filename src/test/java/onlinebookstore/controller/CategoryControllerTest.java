package onlinebookstore.controller;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import onlinebookstore.dto.category.CategoryDto;
import onlinebookstore.dto.category.CategoryRequestDto;
import onlinebookstore.exception.EntityNotFoundException;
import onlinebookstore.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Sql(scripts = {"classpath:database/add-categories-to-database.sql",})
@Sql(scripts = {"classpath:database/delete-all-from-database.sql",
        "classpath:database/reset-autoincrement.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Create category with valid request")
    @WithMockUser(username = "admin@test.com", roles = {"USER", "ADMIN"})
    void createCategory_validRequestDto_returnsDto() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto(
                "Tales", "Some description");

        MvcResult mvcResult = mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), CategoryDto.class);

        assertThat(actual)
                .hasFieldOrPropertyWithValue("name", "Tales");
    }

    @Test
    @DisplayName("Get category by valid id")
    @WithMockUser(username = "admin@test.com", roles = {"USER", "ADMIN"})
    void getCategory_byValidId_returnsDto() throws Exception {
        Long categoryId = 1L;

        CategoryDto categoryDto = new CategoryDto(
                1L, "Education", "Some description");

        MvcResult mvcResult = mockMvc.perform(get("/categories/" + categoryId))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), CategoryDto.class);

        assertThat(actual).isNotNull()
                .hasFieldOrPropertyWithValue("id", categoryId)
                .hasFieldOrPropertyWithValue("name",categoryDto.name())
                .hasFieldOrPropertyWithValue("description", categoryDto.description());
    }

    @Test
    @DisplayName("Get book by incorrect id")
    @WithMockUser(username = "admin@test.com", roles = {"USER", "ADMIN"})
    void getCategory_byIncorrectId_throwsException() throws Exception {
        Long categoryId = 10L;

        MvcResult mvcResult = mockMvc.perform(get("/categories/" + categoryId))
                .andExpect(status().isNotFound())
                .andReturn();

        Exception resolvedException = mvcResult.getResolvedException();
        Assertions.assertNotNull(resolvedException);
        Assertions.assertTrue(resolvedException instanceof EntityNotFoundException);
    }

    @Test
    @DisplayName("Delete category by valid id")
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void deleteCategory_byValidId() throws Exception {
        Long categoryId = 1L;

        mockMvc.perform(delete("/categories/" + categoryId))
                .andExpect(status().isNoContent())
                .andReturn();

        assertThat(categoryRepository.findById(categoryId)).isEmpty();
    }
}
