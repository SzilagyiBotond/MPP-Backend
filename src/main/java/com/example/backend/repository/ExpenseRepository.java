package com.example.backend.repository;

import com.example.backend.model.Expense;
import com.example.backend.model.Person;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    @Transactional
    void deleteByPaid(Person person);
}
