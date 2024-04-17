package com.example.backend.repository;


import com.example.backend.model.Expense;
import com.example.backend.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    @Bean
    CommandLineRunner initDatabase(PersonRepository personRepository,ExpenseRepository expenseRepository){
        return args -> {
            log.info("Preloading "+personRepository.save(new Person("Gabi","alive","123456")));
            log.info("Preloading "+personRepository.save(new Person("Berni","alive","1234567")));
            log.info("Preloading "+personRepository.save(new Person("Boti","alive","1234568")));
            log.info("Preloading "+expenseRepository.save(new Expense("Lidl",10.,personRepository.getById((long) 1),"Alma", "2024-02-18","LEI")));
        };
    }
}
