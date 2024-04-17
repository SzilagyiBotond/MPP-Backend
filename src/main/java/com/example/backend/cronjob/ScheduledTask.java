package com.example.backend.cronjob;

import com.example.backend.controller.ExpenseController;
import com.example.backend.model.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class ScheduledTask {
    @Autowired
    private ExpenseController expenseController;

    private final List<String> names = Arrays.asList("Auchan","Lidl","Kaufland");
    private final List<String> paidBy= Arrays.asList("Gabi","Boti","Berni");

    private final LocalDate dayStart=LocalDate.of(2024,1,1);
    private final LocalDate dayEnd=LocalDate.of(2024,4,8);

    private final Random random=new Random();

//    @Scheduled(cron = "0 * * * * *")
//    public void generateExpense(){
//        expenseController.addExpense(new Expense(names.get(random.nextInt(names.size())),random.nextDouble(),paidBy.get(random.nextInt(paidBy.size())),"", LocalDate.ofEpochDay(ThreadLocalRandom.current().nextLong(dayStart.toEpochDay(),dayEnd.toEpochDay())).toString(),"LEI"));
//    }
}
