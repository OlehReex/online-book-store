package onlinebookstore.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.Optional;
import onlinebookstore.dto.category.CategoryDto;
import onlinebookstore.dto.category.CategoryRequestDto;
import onlinebookstore.exception.EntityNotFoundException;
import onlinebookstore.mapper.CategoryMapper;
import onlinebookstore.mapper.impl.CategoryMapperImpl;
import onlinebookstore.model.Category;
import onlinebookstore.repository.CategoryRepository;
import onlinebookstore.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Spy
    private CategoryMapper categoryMapper = new CategoryMapperImpl();

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Save valid category")
    void saveCategory_validRequest_returnCategoryDto() {
        Long categoryId = 1L;

        Category category = createCategory(categoryId);

        CategoryRequestDto requestDto = new CategoryRequestDto(
                category.getName(), category.getDescription());

        Mockito.when(categoryMapper.toCategory(requestDto)).thenReturn(category);
        Mockito.when(categoryRepository.save(category)).thenReturn(category);

        CategoryDto actual = categoryService.save(requestDto);

        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", category.getId())
                .hasFieldOrPropertyWithValue("name", category.getName())
                .hasFieldOrPropertyWithValue("description", category.getDescription());
    }

    @Test
    @DisplayName("Find category by valid id")
    void getCategory_byValidId_returnCategoryDto() {
        Long categoryId = 1L;

        Category category = createCategory(categoryId);

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        CategoryDto actual = categoryService.getById(categoryId);

        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", category.getId())
                .hasFieldOrPropertyWithValue("name", category.getName())
                .hasFieldOrPropertyWithValue("description", category.getDescription());
    }

    @Test
    @DisplayName("Find category by incorrect id")
    void getCategory_byIncorrectId_throwsException() {
        Long categoryId = 10L;

        Mockito.when(categoryRepository.findById(categoryId))
                .thenThrow(EntityNotFoundException.class);

        assertThatThrownBy(() -> categoryService.getById(categoryId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("Update category")
    void updateCategory_validRequest_returnCategoryDto() {
        Long categoryId = 1L;

        Category category = createCategory(categoryId);

        CategoryRequestDto requestDto = new CategoryRequestDto(
                category.getName(), "New description");

        Category updatedCategory = new Category();
        updatedCategory.setId(category.getId());
        updatedCategory.setName(category.getName());
        updatedCategory.setDescription(requestDto.description());

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.save(category)).thenReturn(updatedCategory);

        CategoryDto actual = categoryService.update(categoryId, requestDto);

        assertThat(actual)
                .hasFieldOrPropertyWithValue("id", updatedCategory.getId())
                .hasFieldOrPropertyWithValue("name", updatedCategory.getName())
                .hasFieldOrPropertyWithValue("description", updatedCategory.getDescription());
    }

    @Test
    @DisplayName("Delete category by walid id")
    void deleteCategory_byValidId() {
        Long categoryId = 1L;

        Mockito.doNothing().when(categoryRepository).deleteById(categoryId);

        categoryService.deleteById(categoryId);

        Mockito.verify(categoryRepository, Mockito.times(1)).deleteById(categoryId);
        Mockito.verifyNoMoreInteractions(categoryRepository);
    }

    private Category createCategory(Long categoryId) {
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Education");
        category.setDescription("Some description");
        return category;
    }
}
