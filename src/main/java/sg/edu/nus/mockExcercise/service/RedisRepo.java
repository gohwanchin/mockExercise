package sg.edu.nus.mockExcercise.service;

import java.util.List;

import org.springframework.stereotype.Repository;

import sg.edu.nus.mockExcercise.model.Book;

@Repository
public interface RedisRepo {
    public void saveBook(final Book book);
    public Book findByBookId(final String bookId);
    public List<Book> findBySearchTerm(final String searchTerm);
}
