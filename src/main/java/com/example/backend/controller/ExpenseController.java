package com.example.backend.controller;

import com.example.backend.exceptions.PersonNotFoundException;
import com.example.backend.model.Person;
import com.example.backend.repository.ExpenseRepository;
import com.example.backend.exceptions.ExpenseNotFoundException;
import com.example.backend.model.Expense;
import com.example.backend.repository.PersonRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@CrossOrigin
public class ExpenseController {
    private final List<String> names = Arrays.asList("Auchan","Lidl","Kaufland");
//    private final List<String> paidBy= Arrays.asList("Gabi","Boti","Berni");

    private final LocalDate dayStart=LocalDate.of(2024,1,1);
    private final LocalDate dayEnd=LocalDate.of(2024,4,8);

    private final Random random=new Random();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final ExpenseRepository repository;

    private final PersonRepository personRepository;

    public ExpenseController(ExpenseRepository repository, PersonRepository personRepository) {
        this.repository = repository;
        this.personRepository = personRepository;
    }
    @GetMapping("/persons")
    List<Person> allPerson(){return personRepository.findAll();}

    @PostMapping("/persons")
    @ResponseStatus(HttpStatus.CREATED)
    Person newPerson(@RequestBody Person person){
        return personRepository.save(person);
    }

    @GetMapping("/persons/{id}")
    Person onePerson(@PathVariable Long id){
        return personRepository.findById(id).orElseThrow(()->new PersonNotFoundException(id));
    }

    @PutMapping("/persons/{id}")
    Person replacePerson(@RequestBody Person newPerson,@PathVariable Long id){
        return personRepository.findById(id)
                .map(person  -> {
                    person.setName(newPerson.getName());
                    person.setStatus(newPerson.getStatus());
                    person.setRevolutId(newPerson.getRevolutId());
                    return personRepository.save(person);
                    //return person;
                }).orElseGet(()->{
                    newPerson.setId(id);
                    return personRepository.save(newPerson);
                });
    }
    @DeleteMapping("/persons/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deletePerson(@PathVariable Long id){
        if ((Object) personRepository.findById(id)== Optional.empty()){
            throw new PersonNotFoundException(id);
        }
        repository.deleteByPaid(personRepository.getReferenceById(id));
        personRepository.deleteById(id);
    }

    @GetMapping("/expenses")
    List<Expense> allExpense(){
        return repository.findAll();
    }

    @PostMapping("/expenses")
    @ResponseStatus(HttpStatus.CREATED)
    Expense newExpense(@Valid @RequestBody Expense expense){
        System.out.println(expense);
        return repository.save(expense);
    }

    @MessageMapping("/generated")
    @SendTo("/topic/generated")
    public Expense sendExpense(Expense expense){
        return expense;
    }

    @Scheduled(fixedRate = 20000)
    public void generateExpense(){
        messagingTemplate.convertAndSend("/topic/generated",repository.save(new Expense(names.get(random.nextInt(names.size())),random.nextDouble(),personRepository.findAll().get(random.nextInt((int) personRepository.count())),"", LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(dayStart.toEpochDay(),dayEnd.toEpochDay())).toString(),"LEI")));
    }

//    @SendTo("/expenses/generated")
//    public Expense addExpense(@Valid Expense expense){
//        return repository.save(expense);
////        messagingTemplate.convertAndSend("/expenses/generated",repository.save(expense));
//    }

    @GetMapping("/expenses/{id}")
    Expense one(@PathVariable Long id){
        return repository.findById(id).orElseThrow(()->new ExpenseNotFoundException(id));
    }

    @PutMapping("/expenses/{id}")
    Expense replaceExpense(@Valid @RequestBody Expense newExpense,@PathVariable Long id){
        return repository.findById(id)
                .map(expense -> {
                    expense.setName(newExpense.getName());
                    expense.setPrice(newExpense.getPrice());
                    expense.setPaid(newExpense.getPaid());
                    expense.setCurrency(newExpense.getCurrency());
                    expense.setDescription(newExpense.getDescription());
                    expense.setDate(newExpense.getDate());
                    return repository.save(expense);
                    //return expense;
                }).orElseGet(()->{
                    newExpense.setId(id);
                    return repository.save(newExpense);
                });
    }
    @DeleteMapping("/expenses/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteExpense(@PathVariable Long id){
        if ((Object) repository.findById(id)== Optional.empty()){
            throw new ExpenseNotFoundException(id);
        }
        repository.deleteById(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = (( FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
