package ru.frolov.springboot.Project2Boot.controller;


import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.frolov.springboot.Project2Boot.models.Book;
import ru.frolov.springboot.Project2Boot.models.Person;
import ru.frolov.springboot.Project2Boot.services.BooksService;
import ru.frolov.springboot.Project2Boot.services.PeopleService;


@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;

    public BooksController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(@RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer itemPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sortByYear,
                        Model model){
        if (page == null || itemPerPage == null){
            model.addAttribute("books", booksService.findAllBooks(sortByYear));
        } else
            model.addAttribute("books", booksService.findWithPagination(page, itemPerPage, sortByYear));

        return "/books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person){
        model.addAttribute("book", booksService.findOneBook(id));

        Person personSearch = booksService.getBookOwner(id);
        if (personSearch == null){
            model.addAttribute("people", peopleService.findAllPeople());
        }
        else model.addAttribute("owner", personSearch);

        return "/books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book){
        return "books/new";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult){
        if (bindingResult.hasErrors()) return "/books/new";

        booksService.saveBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id, Model model){
        model.addAttribute("book", booksService.findOneBook(id));
        return "/books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         @PathVariable("id") int id, BindingResult bindingResult){

        if (bindingResult.hasErrors()) return "/books/edit";

        booksService.updateBook(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person assignPerson){
        booksService.assign(id, assignPerson);

        return "redirect:/books";
    }

    @PatchMapping("/{id}/remove")
    public String remove(@PathVariable("id") int id){
        booksService.release(id);

        return "redirect:/books" + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        booksService.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String search(){
        return "books/search";
    }

    @PostMapping("/search")
    public String makeSearch(@RequestParam(name = "title") String title, Model model){
        model.addAttribute("books", booksService.findBookTitle(title));
        return "books/search";
    }
}
