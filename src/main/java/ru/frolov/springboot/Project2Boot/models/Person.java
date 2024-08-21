package ru.frolov.springboot.Project2Boot.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

@Entity
@Table(name = "Person")
public class Person {

    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int personId;

    @Column(name = "full_name")
    @NotEmpty(message = "Поле 'Имя пользователя' не должно быть пустым")
    private String fullName;

    @Min(value = 1950, message = "Дата рождения должна быть > 1950")
    @Column(name = "year_of_birth")
    private int yearOfBirth;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person(){
    }

    public Person( String fullName) {
        this.fullName = fullName;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Person{" +
                "personId=" + personId +
                ", fullName='" + fullName + '\'' +
                ", dateOfBirth=" + yearOfBirth +
                '}';
    }
}
