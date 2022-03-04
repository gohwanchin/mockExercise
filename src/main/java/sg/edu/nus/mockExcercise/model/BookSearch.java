package sg.edu.nus.mockExcercise.model;

import org.springframework.stereotype.Controller;

public class BookSearch {
    private String searchTerm;

    public BookSearch(){
        this.searchTerm = null;
    }

    
    public BookSearch(String searchTerm) {
        this.searchTerm = searchTerm;
    }


    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

}
