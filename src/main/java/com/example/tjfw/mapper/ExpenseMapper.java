package com.example.tjfw.mapper;

import com.example.tjfw.dto.expense.ExpenseDTO;
import com.example.tjfw.entity.Expense;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {

    public ExpenseDTO toDTO(Expense expense) {
        return new ExpenseDTO(
                expense.getId(),
                expense.getExpenseName(),
                expense.getExpenseType(),
                expense.getExpenseDate(),
                expense.getAmount(),
                expense.getNotes()
        );
    }
}