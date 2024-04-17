package com.example.backend.exceptions;

public class ExpenseNotFoundException extends RuntimeException{
    public ExpenseNotFoundException(Long id){
        super("Cannot find expense" + id);
    }
}
