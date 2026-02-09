package com.example.tjfw.controller;

import com.example.tjfw.dto.expense.ExpenseDTO;
import com.example.tjfw.dto.expense.RequestExpenseDTO;
import com.example.tjfw.response.ApiResponse;
import com.example.tjfw.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExpenseDTO>>> listAll() {
        return ResponseEntity.ok(new ApiResponse<>("List of expenses found", expenseService.list()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponse<>("Expense found", expenseService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseDTO>> create(@Valid @RequestBody RequestExpenseDTO request) {
        return ResponseEntity.status(201).body(new ApiResponse<>("Expense created successfully", expenseService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ExpenseDTO>> update(@PathVariable Long id, @Valid @RequestBody RequestExpenseDTO request) {
        return ResponseEntity.ok(new ApiResponse<>("Expense updated successfully", expenseService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        expenseService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>("Expense deleted successfully", null));
    }
}
