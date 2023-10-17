package onlinebookstore.repository.impl;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import onlinebookstore.exception.DataProcessingException;
import onlinebookstore.exception.EntityNotFoundException;
import onlinebookstore.model.Book;
import onlinebookstore.repository.BookRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Override
    public Book save(Book book) {
        try {
            sessionFactory.inTransaction(session -> session.persist(book));
        } catch (Exception e) {
            throw new DataProcessingException("Can't save book to DB");
        }
        return book;
    }

    @Override
    public List<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Book", Book.class).getResultList();
        } catch (Exception e) {
            throw new EntityNotFoundException("Can't get books from DB", e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        try {
            return sessionFactory.fromSession(s -> Optional.ofNullable(s.get(Book.class, id)));
        } catch (Exception e) {
            throw new EntityNotFoundException("Can't find book with id" + id);
        }
    }
}
