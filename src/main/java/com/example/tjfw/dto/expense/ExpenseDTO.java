package com.example.tjfw.dto.expense;

import com.example.tjfw.entity.ExpenseType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseDTO(
        Long id,
        String expenseName,
        ExpenseType expenseType,
        LocalDate expenseDate,
        BigDecimal amount,
        String notes
) {}
