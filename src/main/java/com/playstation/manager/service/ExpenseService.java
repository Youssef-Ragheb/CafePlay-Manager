package com.playstation.manager.service;

import com.playstation.manager.dto.ExpenseDTO;
import com.playstation.manager.entity.Expense;
import com.playstation.manager.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserService userService;
    private final ActionLogService actionLogService;

    public Expense create(ExpenseDTO dto) {
        Expense expense = new Expense();
        expense.setAmount(dto.getAmount());
        expense.setReason(dto.getReason());
        expense.setDateTime(LocalDateTime.now());
        expense.setCreatedBy(userService.getCurrentUser());
        Expense savedExpense = expenseRepository.save(expense);
        actionLogService.log("CREATE", "Expense", savedExpense.getId());
        return expenseRepository.save(expense);
    }

    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }
    public Expense findById(Long id) {
        return expenseRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        actionLogService.log("DELETE", "Expense", id);
        expenseRepository.deleteById(id);
    }
}