package com.example.backend.validator;

import com.example.backend.model.Person;
import com.example.backend.repository.PersonRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import java.time.Period;

@Component
public class ExistingPersonValidator implements ConstraintValidator<ExistingPerson, Person> {

//    private static final ExistingPersonValidator holder=new ExistingPersonValidator();
//
//    @Bean
//    public static ExistingPersonValidator bean(PersonRepository personRepository){
//        holder.personRepository=personRepository;
//        return holder;
//    }

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void initialize(ExistingPerson constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public boolean isValid(Person person, ConstraintValidatorContext constraintValidatorContext) {
        return personRepository.existsById(person.getId());
    }
}
