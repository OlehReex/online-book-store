package onlinebookstore.mapper;

import java.util.stream.Collectors;
import onlinebookstore.dto.book.BookDto;
import onlinebookstore.dto.book.BookDtoWithoutCategoryIds;
import onlinebookstore.dto.book.CreateBookRequestDto;
import onlinebookstore.model.Book;
import onlinebookstore.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;

import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl"
)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toBook(CreateBookRequestDto bookRequestDto);

    BookDtoWithoutCategoryIds toBookWithoutCategories(Book book);

    void updateBook(CreateBookRequestDto updatedRequestDto, @MappingTarget Book bookToUpdate);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoriesIds(
                book.getCategories().stream()
                        .map(Category::getId)
                        .toList());
    }

    @AfterMapping
    default void setCategories(@MappingTarget Book book, CreateBookRequestDto bookDto) {
        book.setCategories(bookDto.categoriesIds().stream()
                .map(Category::new)
                .collect(Collectors.toSet()));
    }
}
