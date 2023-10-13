package onlinebookstore.repository.impl;

import java.util.List;
import onlinebookstore.model.Book;
import onlinebookstore.repository.BookRepository;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public BookRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Book save(Book book) {
        sessionFactory.inTransaction(session -> session.persist(book));
        return book;
    }

    @Override
    public List<Book> findAll() {
        try {
            Query<Book> fromUser = sessionFactory
                    .openSession().createQuery("FROM Book", Book.class);
            return fromUser.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Can't get books from DB", e);
        }
    }
}
