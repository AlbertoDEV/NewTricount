package com.example.gastos.controller;

import com.example.gastos.model.Debt;
import com.example.gastos.model.User;
import com.example.gastos.repository.DebtRepository;
import com.example.gastos.service.DebtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class DebtController {

    @Autowired
    private DebtService debtService;

    @PostMapping("/debts/{id}/notify")
    public String notifyPayment(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        debtService.sendPaidNotification(id);
        // We need the group id to redirect back to the group page.
        // This is a bit tricky. For now, I'll redirect to the dashboard.
        // A better solution would be to pass the group id in the request.
        return "redirect:/dashboard";
    }

    @PostMapping("/debts/{id}/confirm")
    public String confirmPayment(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        debtService.confirmPayment(id);
        return "redirect:/dashboard";
    }
}
