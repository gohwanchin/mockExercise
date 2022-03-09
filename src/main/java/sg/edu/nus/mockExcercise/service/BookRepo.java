package sg.edu.nus.mockExcercise.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import sg.edu.nus.mockExcercise.model.Book;

@Service
public class BookRepo implements RedisRepo {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    private Logger logger = Logger.getLogger(BookRepo.class.getName());

    @Override
    public void saveBook(final Book book) {
        redisTemplate.opsForList().leftPush("bookList", book.getId());
        redisTemplate.opsForHash().put("bookList_Map", book.getId(), book);
    }

    @Override
    public Book findByBookId(final String bookId) {
        Book result = (Book) redisTemplate.opsForHash()
                .get("bookList_Map", bookId);
        return result;
    }

    @Override
    public List<Book> findBySearchTerm(final String titleSearch, final String authorSearch) {
        // Gets all book IDs
        List<Object> fromBookList = redisTemplate.opsForList()
                .range("bookList", 0, -1);
        logger.log(Level.INFO, "Book IDs obtained");
        // Casts objects from redis database to Book object to create list of books
        List<Book> books = (List<Book>) redisTemplate.opsForHash()
                .multiGet("bookList_Map", fromBookList).stream()
                .filter(Book.class::isInstance).map(Book.class::cast)
                .toList();
        logger.log(Level.INFO, "Books obtained");
        // Filters book list based on search terms (case-insenstive) and default sort
        // title A-Z
        List<Book> bookResults = books.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(titleSearch.toLowerCase()))
                .filter(book -> book.getAuthor().toLowerCase().contains(authorSearch.toLowerCase()))
                .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());

        logger.log(Level.INFO, "Books filtered");

        return bookResults;
    }

    @Override
    public List<Book> sortingBooks(List<Book> bookList, String field, Boolean alphabetical) {
        // Sorts list based on field provided
        Comparator<Book> comparator = (b1, b2) -> b1.getField(field).compareTo(b2.getField(field));
        if (alphabetical) {
            bookList.sort(comparator);
        } else {
            bookList.sort(comparator.reversed());
        }
        return bookList;
    }

    @Override
    public Page<Book> pagination(List<Book> bookList, int page, int size) {
        int startItem = page * size;
        Pageable pageable = PageRequest.of(page, size);
        List<Book> list;

        // Creaing sublist with parameters given
        if (bookList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + size, bookList.size());
            list = bookList.subList(startItem, toIndex);
        }

        // Creating Page object out of sublist
        Page<Book> bookPage = new PageImpl<>(list, pageable, bookList.size());
        return bookPage;
    }

}
