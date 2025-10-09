package com.example.gastos.service;

import com.example.gastos.model.*;
import com.example.gastos.repository.DebtRepository;
import com.example.gastos.repository.ExpenseRepository;
import com.example.gastos.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Transactional
    public void addExpense(Group group, User paidBy, String description, BigDecimal amount) {
        Expense expense = new Expense();
        expense.setGroup(group);
        expense.setPaidBy(paidBy);
        expense.setDescription(description);
        expense.setAmount(amount);
        expense.setDate(LocalDateTime.now());
        expenseRepository.save(expense);

        recalculateDebts(group);
    }

    private void recalculateDebts(Group group) {
        // Re-fetch the group to ensure the members list is populated
        Group freshGroup = groupRepository.findById(group.getId())
                .orElseThrow(() -> new RuntimeException("Group not found during debt recalculation"));

        // 1. Clear existing debts for the group
        List<Debt> existingDebts = debtRepository.findByGroup(freshGroup);
        debtRepository.deleteAll(existingDebts);

        // 2. Calculate balances
        Map<User, BigDecimal> balances = new HashMap<>();
        if (freshGroup.getMembers() == null || freshGroup.getMembers().isEmpty()) {
            return; // No members, no debts to calculate
        }
        for (GroupMember member : freshGroup.getMembers()) {
            balances.put(member.getUser(), BigDecimal.ZERO);
        }

        List<Expense> expenses = expenseRepository.findByGroup(freshGroup);
        BigDecimal totalExpenses = expenses.stream().map(Expense::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal share = totalExpenses.divide(new BigDecimal(freshGroup.getMembers().size()), 2, RoundingMode.HALF_UP);

        for (Expense expense : expenses) {
            balances.merge(expense.getPaidBy(), expense.getAmount(), BigDecimal::add);
        }

        for (Map.Entry<User, BigDecimal> entry : balances.entrySet()) {
            balances.put(entry.getKey(), entry.getValue().subtract(share));
        }

        // 3. Create new debts
        Map<User, BigDecimal> debtors = new HashMap<>();
        Map<User, BigDecimal> creditors = new HashMap<>();

        for (Map.Entry<User, BigDecimal> entry : balances.entrySet()) {
            if (entry.getValue().compareTo(BigDecimal.ZERO) < 0) {
                debtors.put(entry.getKey(), entry.getValue().abs());
            } else if (entry.getValue().compareTo(BigDecimal.ZERO) > 0) {
                creditors.put(entry.getKey(), entry.getValue());
            }
        }

        for (Map.Entry<User, BigDecimal> debtorEntry : debtors.entrySet()) {
            User debtor = debtorEntry.getKey();
            BigDecimal amountToPay = debtorEntry.getValue();

            for (Map.Entry<User, BigDecimal> creditorEntry : creditors.entrySet()) {
                User creditor = creditorEntry.getKey();
                BigDecimal amountOwed = creditorEntry.getValue();

                if (amountToPay.compareTo(BigDecimal.ZERO) > 0 && amountOwed.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal payment = amountToPay.min(amountOwed);

                    Debt debt = new Debt();
                    debt.setGroup(freshGroup);
                    debt.setDebtor(debtor);
                    debt.setCreditor(creditor);
                    debt.setAmount(payment);
                    debtRepository.save(debt);

                    amountToPay = amountToPay.subtract(payment);
                    creditorEntry.setValue(amountOwed.subtract(payment));
                }
            }
        }
    }

    public List<Expense> findExpensesByGroup(Group group) {
        return expenseRepository.findByGroup(group);
    }

    public List<Debt> findDebtsByGroup(Group group) {
        return debtRepository.findByGroup(group);
    }
}
