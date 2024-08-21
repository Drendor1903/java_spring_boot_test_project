package ru.frolov.springboot.Project2Boot.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.frolov.springboot.Project2Boot.models.Book;
import ru.frolov.springboot.Project2Boot.models.Person;
import ru.frolov.springboot.Project2Boot.repositories.BooksRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }


    public List<Book> findAllBooks(boolean sortByYear){
        if (sortByYear)
            return booksRepository.findAll(Sort.by("yearOfRelease"));
        else
            return booksRepository.findAll();
    }

    public Book findOneBook(int id){
        Optional<Book> book = booksRepository.findById(id);
        return book.orElse(null);
    }

    @Transactional
    public void saveBook(Book saveBook){
        booksRepository.save(saveBook);
    }

    public Person getBookOwner(int id){
        return booksRepository.findById(id).map(Book::getOwner).orElse(null);
    }
    @Transactional
    public void assign(int id, Person person) {
        booksRepository.findById(id).ifPresent(book->{
            book.setTakenAt(new Date());
            book.setOwner(person);
        });
    }
    @Transactional
    public void release(int id) {
        booksRepository.findById(id).ifPresent(
                book -> {
                    book.setTakenAt(null);
                    book.setOwner(null);
                });
    }

    @Transactional
    public void updateBook(int id, Book updateBook){
        Book bookToBeUpdated = booksRepository.findById(id).get();
        updateBook.setBookId(id);
        updateBook.setOwner(bookToBeUpdated.getOwner());
        booksRepository.save(updateBook);
    }

    @Transactional
    public void deleteBook(int id){
        booksRepository.deleteById(id);
    }

    public List<Book> findBookTitle(String title){
        return booksRepository.findByBookNameStartingWith(title);
    }

    public List<Book> findWithPagination(int page, int itemPerPage, boolean sortByYear){
        if (sortByYear)
            return booksRepository.findAll(PageRequest.of(page, itemPerPage, Sort.by("yearOfRelease"))).getContent();
        else
            return booksRepository.findAll(PageRequest.of(page, itemPerPage)).getContent();
    }
}
