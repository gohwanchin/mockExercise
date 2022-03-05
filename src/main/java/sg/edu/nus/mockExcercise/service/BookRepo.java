package sg.edu.nus.mockExcercise.service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import sg.edu.nus.mockExcercise.model.Book;

@Service
public class BookRepo implements RedisRepo {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(final Book book){
        redisTemplate.opsForList().leftPush("bookList", book.getId());
        redisTemplate.opsForHash().put("bookList_Map",book.getId(),book);
    }

    @Override
    public Book findById(final String bookId){
        Book result = (Book)redisTemplate.opsForHash()
            .get("bookList_Map", bookId);
        return result;
    }

    @Override
    public List<Book> findBySearchTerm(final String searchTerm){
        List<Object> fromBookList = redisTemplate.opsForList()
            .range("bookList", 0, 9);
        List<Book> books = (List<Book>) redisTemplate.opsForHash()
            .multiGet("bookList_Map", fromBookList).stream()
            .filter(Book.class::isInstance).map(Book.class::cast)
            .toList();
        List<Book> bookResults = books.stream().filter(book -> searchTerm.contains(book.getTitle())).collect(Collectors.toList());
        return bookResults;
    }
}
