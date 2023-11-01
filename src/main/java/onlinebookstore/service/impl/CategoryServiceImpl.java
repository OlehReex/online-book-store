package onlinebookstore.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.category.CategoryDto;
import onlinebookstore.dto.category.CategoryRequestDto;
import onlinebookstore.exception.EntityNotFoundException;
import onlinebookstore.mapper.CategoryMapper;
import onlinebookstore.model.Category;
import onlinebookstore.repository.CategoryRepository;
import onlinebookstore.service.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::categoryToResponseDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.categoryToResponseDto(categoryRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find category with id " + id)));
    }

    @Override
    public CategoryDto save(CategoryRequestDto requestDto) {
        Category categoryToSave = categoryMapper.toCategory(requestDto);
        categoryRepository.save(categoryToSave);
        return categoryMapper.categoryToResponseDto(categoryToSave);
    }

    @Override
    public CategoryDto update(Long id, CategoryRequestDto requestDto) {
        Category categoryToUpdate = categoryRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find category with id " + id));
        categoryMapper.updateCategory(requestDto, categoryToUpdate);
        return categoryMapper.categoryToResponseDto(categoryRepository.save(categoryToUpdate));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
