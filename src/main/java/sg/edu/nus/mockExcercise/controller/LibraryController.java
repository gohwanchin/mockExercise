package sg.edu.nus.mockExcercise.controller;

import sg.edu.nus.mockExcercise.model.Book;
import sg.edu.nus.mockExcercise.model.BookSearch;
import sg.edu.nus.mockExcercise.service.BookRepo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LibraryController {
    @Autowired
    BookRepo service;

    @GetMapping("/")
    public String idSearch(){
        return "idSearch";
    }

    @GetMapping("/idSearch")
    public String findById(@RequestParam(name = "bookId") String bookId, Model model){
        Book book = service.findById(bookId);
        model.addAttribute("book", book);
        return "showBook";
    }

    @PostMapping("/addBook")
    public String addBook(@ModelAttribute Book book, Model model){
        service.save(book);
        model.addAttribute("book", book);
        return "showBook";
    }

    @GetMapping("/addBook")
    public String bookForm(Model model){
        model.addAttribute("book", new Book());
        return "addBook";
    }

    @GetMapping("/titleSearch")
    public String findByTitle(@RequestParam(name = "searchTerm") String searchTerm, Model model){
        List<Book> books = service.findBySearchTerm(searchTerm);
        model.addAttribute("books", books);
        return "titleSearch";
    }
}
