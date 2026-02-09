package com.example.tjfw.service;

import com.example.tjfw.dto.expense.ExpenseDTO;
import com.example.tjfw.dto.expense.RequestExpenseDTO;
import com.example.tjfw.entity.Expense;
import com.example.tjfw.exceptions.NotFoundException;
import com.example.tjfw.repository.ExpenseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<ExpenseDTO> list() {
        return expenseRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public ExpenseDTO getById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Expense not found with id "+ id));
        return toDTO(expense);
    }

    public ExpenseDTO create(RequestExpenseDTO request) {
        Expense expense = new Expense(
                request.getExpenseName(),
                request.getExpenseType(),
                request.getExpenseDate(),
                request.getAmount(),
                request.getNotes()
        );
        Expense saved = expenseRepository.save(expense);
        return toDTO(saved);
    }

    public ExpenseDTO update(Long id, RequestExpenseDTO request) {
        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Expense not found with id "+ id));

        existing.setExpenseName(request.getExpenseName());
        existing.setExpenseType(request.getExpenseType());
        existing.setExpenseDate(request.getExpenseDate());
        existing.setAmount(request.getAmount());
        existing.setNotes(request.getNotes());

        Expense updated = expenseRepository.save(existing);
        return toDTO(updated);
    }

    public void delete(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new NotFoundException("Expense not found with id "+ id);
        }
        expenseRepository.deleteById(id);
    }

    // ========================
    // Mapper
    // ========================
    private ExpenseDTO toDTO(Expense expense) {
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
