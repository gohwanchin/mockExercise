package sg.edu.nus.mockExcercise.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import sg.edu.nus.mockExcercise.model.Book;

@Repository
public interface RedisRepo {
    public void saveBook(final Book book);

    public Book findByBookId(final String bookId);

    public List<Book> findBySearchTerm(String titleSearch, String authorSearch);

    public List<Book> sortingBooks(List<Book> bookList, String field, Boolean alphabetical);

    public Page<Book> pagination(List<Book> bookList, int page, int size);
}
