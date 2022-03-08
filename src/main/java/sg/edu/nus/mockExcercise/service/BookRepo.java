package sg.edu.nus.mockExcercise.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import sg.edu.nus.mockExcercise.model.Book;

@Service
public class BookRepo implements RedisRepo {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

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
    public Page<Book> findBySearchTerm(final String searchTerm, final int page, final int size) {
        // Gets all book IDs
        List<Object> fromBookList = redisTemplate.opsForList()
                .range("bookList", 0, -1);
        // Casts objects from redis database to Book object to create list of books
        List<Book> books = (List<Book>) redisTemplate.opsForHash()
                .multiGet("bookList_Map", fromBookList).stream()
                .filter(Book.class::isInstance).map(Book.class::cast)
                .toList();
        // Filters book list
        List<Book> bookResults = books.stream()
                .filter(book -> book.getTitle().contains(searchTerm))
                // .sorted(Comparator.comparing(Book::getTitle))
                .collect(Collectors.toList());

        int startItem = page * size;
        List<Book> list;

        if (bookResults.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + size, bookResults.size());
            list = bookResults.subList(startItem, toIndex);
        }

        Page<Book> bookPage = new PageImpl<>(list, PageRequest.of(page, size), bookResults.size());
        return bookPage;
    }
}
