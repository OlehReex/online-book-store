package onlinebookstore;

import java.math.BigDecimal;
import onlinebookstore.model.Book;
import onlinebookstore.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlineBookStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(BookService bookService) {
        return args -> {
            Book newBook = new Book();
            newBook.setAuthor("Chuck Palahniuk");
            newBook.setTitle("Fight Club");
            newBook.setCoverImage("Custom Image");
            newBook.setDescription("Just read it");
            newBook.setPrice(BigDecimal.valueOf(250));
            newBook.setIsbn("913 4515 561");
            bookService.save(newBook);
            bookService.findAll();
            System.out.println(bookService.findAll());
        };
    }
}
