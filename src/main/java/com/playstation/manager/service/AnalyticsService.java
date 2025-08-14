package com.playstation.manager.service;

import com.playstation.manager.entity.ClientDebt;
import com.playstation.manager.entity.Expense;
import com.playstation.manager.entity.Session;
import com.playstation.manager.entity.User;
import com.playstation.manager.repository.ClientDebtRepository;
import com.playstation.manager.repository.ExpenseRepository;
import com.playstation.manager.repository.SessionRepository;
import com.playstation.manager.utilz.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final SessionRepository sessionRepository;
    private final ExpenseRepository expenseRepository;
    private final ClientDebtRepository clientDebtRepository;
    private final AuthUtil authUtil;
    private final UserService userService;

    public Map<String, Object> getSummary(LocalDate start, LocalDate end) {
        User currentUser =  userService.getCurrentUser();
        if(!authUtil.isAdmin(currentUser)) {
            return null;
        }
        double revenue = sessionRepository.findByStartTimeBetween(start.atStartOfDay(), end.atTime(23, 59)).stream().mapToDouble(Session::getTotalPrice).sum();

        double expenses = expenseRepository.findByDateTimeBetween(start.atStartOfDay(), end.atTime(23, 59)).stream().mapToDouble(Expense::getAmount).sum();

        double debts = clientDebtRepository.findByCreatedAtBetween(start.atStartOfDay(), end.atTime(23, 59)).stream().mapToDouble(ClientDebt::getAmount).sum();

        Map<String, Object> result = new HashMap<>();
        result.put("revenue", revenue);
        result.put("expenses", expenses);
        result.put("debts", debts);
        return result;
    }


    public List<Map<String, Object>> getRevenueByDay(LocalDate start, LocalDate end) {
        User currentUser =  userService.getCurrentUser();
        if(!authUtil.isAdmin(currentUser)) {
            return null;
        }
        return sessionRepository.findByStartTimeBetween(start.atStartOfDay(), end.atTime(23, 59)).stream().collect(Collectors.
                groupingBy(s -> s.getStartTime().toLocalDate(), Collectors.summingDouble(s -> s.getTotalPrice()))).entrySet().stream().map(e -> {
            Map<String, Object> map = new HashMap<>();
            map.put("date", e.getKey());
            map.put("revenue", e.getValue());
            return map;
        }).sorted(Comparator.comparing(e -> (LocalDate) e.get("date"))).collect(Collectors.toList());
    }

    public Page<Expense> getExpensesBetween(LocalDate start, LocalDate end, int page, int size) {
        User currentUser =  userService.getCurrentUser();
        if(!authUtil.isAdmin(currentUser)) {
            return null;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateTime").descending());
        return expenseRepository.findByDateTimeBetween(start.atStartOfDay(), end.atTime(23, 59), pageable);
    }

    public Page<ClientDebt> getClientDebts(LocalDate start, LocalDate end, int page, int size) {
        User currentUser =  userService.getCurrentUser();
        if(!authUtil.isAdmin(currentUser)) {
            return null;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return clientDebtRepository.findByCreatedAtBetween(start.atStartOfDay(), end.atTime(23, 59), pageable);
    }

}
