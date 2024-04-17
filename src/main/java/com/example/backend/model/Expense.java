package com.example.backend.model;

import com.example.backend.validator.ExistingPerson;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@Entity
@Table(name = "expense")
public class Expense {
    @SequenceGenerator(
            name = "expense_sequence",
            sequenceName = "expense_sequence",
            allocationSize = 1
    )
    private @Id @GeneratedValue Long id;
    @NotBlank(message = "Name cannot be blank")
    private String name;
    private Double price;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
//    @ExistingPerson
    private Person paid;
    private String description;
    private LocalDate date;
    @NotBlank(message = "Currency cannot be blank")
    private String currency;

    public Expense( String name, Double price, Person paid, String description, String date, String currency) {
        this.name = name;
        this.price = price;
        this.paid = paid;
        this.description = description;
        this.date=LocalDate.parse(date);
        this.currency = currency;
    }

    public Expense() {

    }
    @Transactional
    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", paid='" + paid.getName() + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", currency='" + currency + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Person getPaid() {
        return paid;
    }

    public void setPaid(Person paid) {
        this.paid = paid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
