package ru.frolov.springboot.Project2Boot.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.frolov.springboot.Project2Boot.models.Book;
import ru.frolov.springboot.Project2Boot.models.Person;
import ru.frolov.springboot.Project2Boot.repositories.PeopleRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAllPeople(){
        return peopleRepository.findAll();
    }

    public Person findOnePerson(int id){
        Optional<Person> person = peopleRepository.findById(id);
        return person.orElse(null);
    }

    @Transactional
    public void savePerson(Person person){
        peopleRepository.save(person);
    }

    @Transactional
    public void updatePerson(int id, Person updatePerson){
        updatePerson.setPersonId(id);
        peopleRepository.save(updatePerson);
    }

    @Transactional
    public void deletePerson(int id){
        peopleRepository.deleteById(id);
    }

    public Optional<Person> getPersonByFullName(String fullName){
        return peopleRepository.findByFullName(fullName);
    }

    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        if (person.isPresent()){
            Hibernate.initialize(person.get().getBooks());

            person.get().getBooks().forEach(book -> {
                long diffInMilies = Math.abs(book.getTakenAt().getTime() - new Date().getTime());

                if(diffInMilies > 864000000) book.setExpired(true);
            });
            return person.get().getBooks();
        }
        else {
            return Collections.emptyList();
        }
    }
}
