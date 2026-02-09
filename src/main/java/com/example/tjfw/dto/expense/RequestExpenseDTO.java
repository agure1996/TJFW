package com.example.tjfw.dto.expense;

import com.example.tjfw.entity.ExpenseType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RequestExpenseDTO {

    private String expenseName;
    private ExpenseType expenseType;
    private LocalDate expenseDate;
    private BigDecimal amount;
    private String notes;

    public RequestExpenseDTO() {}

    public RequestExpenseDTO(String expenseName, ExpenseType expenseType, LocalDate expenseDate, BigDecimal amount, String notes) {
        this.expenseName = expenseName;
        this.expenseType = expenseType;
        this.expenseDate = expenseDate;
        this.amount = amount;
        this.notes = notes;
    }

    public String getExpenseName() { return expenseName; }
    public void setExpenseName(String expenseName) { this.expenseName = expenseName; }

    public ExpenseType getExpenseType() { return expenseType; }
    public void setExpenseType(ExpenseType expenseType) { this.expenseType = expenseType; }

    public LocalDate getExpenseDate() { return expenseDate; }
    public void setExpenseDate(LocalDate expenseDate) { this.expenseDate = expenseDate; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
