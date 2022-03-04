package sg.edu.nus.mockExcercise.controller;

import sg.edu.nus.mockExcercise.model.Book;
import sg.edu.nus.mockExcercise.model.BookSearch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LibraryController {
    //@Autowired
    //BookRepo service;

    @GetMapping("/")
    public String idSearch(Model model){
        model.addAttribute("bookSearch", new BookSearch());
        return "idSearch";
    }

    @PostMapping("/idSearch")
    public String idSearchSubmit(@ModelAttribute BookSearch bookSearch, Model model){
        //Book book = service.findById(bookId);
        //model.addAttribute("book", book);
        model.addAttribute("bookIdSearch", bookSearch.getSearchTerm());
        return "showBook";
    }

}
