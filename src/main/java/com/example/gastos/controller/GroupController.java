package com.example.gastos.controller;

import com.example.gastos.model.Debt;
import com.example.gastos.model.Expense;
import com.example.gastos.model.Group;
import com.example.gastos.model.User;
import com.example.gastos.service.ExpenseService;
import com.example.gastos.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<Group> groups = groupService.findGroupsByUser(user);
        model.addAttribute("groups", groups);
        model.addAttribute("user", user);
        return "dashboard";
    }

    @GetMapping("/groups/new")
    public String showCreateGroupForm() {
        return "create-group";
    }

    @PostMapping("/groups/new")
    public String createGroup(@RequestParam String name, @RequestParam String description, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        groupService.createGroup(name, description, user);
        return "redirect:/dashboard";
    }

    @GetMapping("/groups/{id}")
    public String groupDetails(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Group group = groupService.findGroupById(id);
        List<Expense> expenses = expenseService.findExpensesByGroup(group);
        List<Debt> debts = expenseService.findDebtsByGroup(group);

        model.addAttribute("group", group);
        model.addAttribute("expenses", expenses);
        model.addAttribute("debts", debts);
        model.addAttribute("user", user);
        return "group-details";
    }

    @PostMapping("/groups/{id}/add-member")
    public String addMember(@PathVariable Long id, @RequestParam String username, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Group group = groupService.findGroupById(id);
        // For simplicity, only the admin can add members
        if (group.getAdmin().getId().equals(user.getId())) {
            groupService.addMemberByUsername(group, username, false); // New members can't add expenses by default
        }
        return "redirect:/groups/" + id;
    }

    @PostMapping("/groups/{id}/add-expense")
    public String addExpense(@PathVariable Long id, @RequestParam String description, @RequestParam BigDecimal amount, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Group group = groupService.findGroupById(id);
        expenseService.addExpense(group, user, description, amount);
        return "redirect:/groups/" + id;
    }
}
