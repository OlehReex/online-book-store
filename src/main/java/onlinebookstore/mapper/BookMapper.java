package onlinebookstore.mapper;

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
        bookDto.setCategoriesId(
                book.getCategories().stream()
                        .map(Category::getId)
                        .toList());
    }
}
