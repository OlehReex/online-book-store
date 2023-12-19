package onlinebookstore.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.Optional;
import onlinebookstore.dto.category.CategoryDto;
import onlinebookstore.dto.category.CategoryRequestDto;
import onlinebookstore.exception.EntityNotFoundException;
import onlinebookstore.mapper.CategoryMapper;
import onlinebookstore.model.Category;
import onlinebookstore.repository.CategoryRepository;
import onlinebookstore.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Save valid category")
    void saveCategory_validRequest_returnCategoryDto() {
        Long categoryId = 1L;

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Education");
        category.setDescription("Some description");

        CategoryDto categoryDto = new CategoryDto(
                category.getId(), category.getName(), category.getDescription());

        CategoryRequestDto requestDto = new CategoryRequestDto(
                category.getName(), category.getDescription());

        Mockito.when(categoryMapper.toCategory(requestDto)).thenReturn(category);
        Mockito.when(categoryRepository.save(category)).thenReturn(category);
        Mockito.when(categoryMapper.categoryToResponseDto(category)).thenReturn(categoryDto);

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

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Education");
        category.setDescription("Some description");

        CategoryDto categoryDto = new CategoryDto(
                category.getId(), category.getName(), category.getDescription());

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(categoryMapper.categoryToResponseDto(category)).thenReturn(categoryDto);

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

        Category category = new Category();
        category.setId(categoryId);
        category.setName("Education");
        category.setDescription("Some description");

        CategoryRequestDto requestDto = new CategoryRequestDto(
                category.getName(), "New description");

        Category updatedCategory = new Category();
        updatedCategory.setId(category.getId());
        updatedCategory.setName(category.getName());
        updatedCategory.setDescription(requestDto.description());

        CategoryDto categoryDto = new CategoryDto(
                updatedCategory.getId(), updatedCategory.getName(),
                updatedCategory.getDescription());

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.save(category)).thenReturn(updatedCategory);
        Mockito.when(categoryMapper.categoryToResponseDto(updatedCategory)).thenReturn(categoryDto);

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
}
