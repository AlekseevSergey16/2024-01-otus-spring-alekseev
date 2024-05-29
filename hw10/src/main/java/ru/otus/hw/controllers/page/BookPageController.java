package ru.otus.hw.controllers.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BookPageController {

    @GetMapping
    public String listPage() {
        return "book-list-page";
    }

    @GetMapping("/books")
    public String createPage() {
        return "book-form";
    }

    @GetMapping("/books/{id}")
    public String editPage(@PathVariable long id) {
        return "book-form";
    }

}
