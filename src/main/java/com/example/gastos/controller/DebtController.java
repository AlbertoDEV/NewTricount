package com.example.gastos.controller;

import com.example.gastos.model.Debt;
import com.example.gastos.model.User;
import com.example.gastos.repository.DebtRepository;
import com.example.gastos.service.DebtService;
import com.example.gastos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class DebtController {

    @Autowired
    private DebtService debtService;

    @Autowired
    private UserService userService;

    @PostMapping("/debts/{id}/notify")
    public String notifyPayment(@PathVariable Long id, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Debt debt = debtService.findById(id).orElseThrow(() -> new IllegalArgumentException("Debt not found"));
        debtService.sendPaidNotification(id);
        return "redirect:/groups/" + debt.getGroup().getId();
    }

    @PostMapping("/debts/{id}/confirm")
    public String confirmPayment(@PathVariable Long id, Principal principal) {
        User user = userService.findByUsername(principal.getName()).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Debt debt = debtService.findById(id).orElseThrow(() -> new IllegalArgumentException("Debt not found"));
        debtService.confirmPayment(id);
        return "redirect:/groups/" + debt.getGroup().getId();
    }
}
