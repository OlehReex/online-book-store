package onlinebookstore.repository;

import java.util.List;
import java.util.Optional;
import onlinebookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b join fetch b.categories c where c.id = :id")
    List<Book> findAllByCategoryId(@Param("id") Long id, Pageable pageable);

    @EntityGraph(attributePaths = "categories")
    Optional<Book> getBookById(Long id);

    @EntityGraph(attributePaths = "categories")
    Page<Book> findAll(Pageable pageable);
}
