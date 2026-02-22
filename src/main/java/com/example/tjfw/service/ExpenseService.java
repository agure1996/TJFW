package com.example.tjfw.service;

import com.example.tjfw.dto.expense.ExpenseDTO;
import com.example.tjfw.dto.expense.RequestExpenseDTO;
import com.example.tjfw.mapper.ExpenseMapper;
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
    private final ExpenseMapper expenseMapper;

    public ExpenseService(ExpenseRepository expenseRepository, ExpenseMapper expenseMapper) {
        this.expenseRepository = expenseRepository;
        this.expenseMapper = expenseMapper;
    }

    public List<ExpenseDTO> list() {
        return expenseRepository.findAll()
                .stream()
                .map(expenseMapper::toDTO)
                .toList();
    }

    public ExpenseDTO getById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Expense not found with id " + id));
        return expenseMapper.toDTO(expense);
    }

    public ExpenseDTO create(RequestExpenseDTO request) {
        Expense expense = new Expense(
                request.getExpenseName(),
                request.getExpenseType(),
                request.getExpenseDate(),
                request.getAmount(),
                request.getNotes()
        );
        return expenseMapper.toDTO(expenseRepository.save(expense));
    }

    public ExpenseDTO update(Long id, RequestExpenseDTO request) {
        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Expense not found with id " + id));

        existing.setExpenseName(request.getExpenseName());
        existing.setExpenseType(request.getExpenseType());
        existing.setExpenseDate(request.getExpenseDate());
        existing.setAmount(request.getAmount());
        existing.setNotes(request.getNotes());

        return expenseMapper.toDTO(expenseRepository.save(existing));
    }

    public void delete(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new NotFoundException("Expense not found with id " + id);
        }
        expenseRepository.deleteById(id);
    }
}