package sg.edu.nus.mockExcercise.controller;

import sg.edu.nus.mockExcercise.model.Book;
import sg.edu.nus.mockExcercise.service.BookRepo;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LibraryController {
    private Logger logger = Logger.getLogger(LibraryController.class.getName());

    @Autowired
    BookRepo service;

    @GetMapping("/")
    public String idSearch() {
        return "idSearch";
    }

    @GetMapping("/idSearch")
    public String findById(@RequestParam(name = "bookId") String bookId,
            Model model) {
        Book book = service.findByBookId(bookId);
        model.addAttribute("book", book);
        return "showBook";
    }

    @PostMapping("/addBook")
    public String addBook(@ModelAttribute Book book, Model model) {
        service.saveBook(book);
        model.addAttribute("book", book);
        return "showBook";
    }

    @GetMapping("/addBook")
    public String bookForm(Model model) {
        model.addAttribute("book", new Book());
        return "addBook";
    }

    @GetMapping("/search")
    public String searchForm() {
        return "search";
    }

    @GetMapping("/searchResults")
    public String searchBooks(
            @RequestParam(defaultValue = "") String titleSearch,
            @RequestParam(defaultValue = "") String authorSearch,
            @RequestParam(defaultValue = "title") String field,
            @RequestParam(defaultValue = "true") Boolean alphabetical,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size, Model model) {
        List<Book> books = service.findBySearchTerm(titleSearch, authorSearch);
        logger.log(Level.INFO, "Result size: " + books.size());

        books = service.sortingBooks(books, field, alphabetical);
        Page<Book> bookPage = service.pagination(books, page, size);
        model.addAttribute("bookPage", bookPage);

        int totalPages = bookPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("titleSearch", titleSearch);
        model.addAttribute("authorSearch", authorSearch);
        model.addAttribute("field", field);
        model.addAttribute("alphabetical", alphabetical);
        return "searchPage";
    }
}
