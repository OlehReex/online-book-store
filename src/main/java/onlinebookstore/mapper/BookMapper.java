package onlinebookstore.mapper;

import onlinebookstore.dto.BookDto;
import onlinebookstore.dto.CreateBookRequestDto;
import onlinebookstore.model.Book;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
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
}
