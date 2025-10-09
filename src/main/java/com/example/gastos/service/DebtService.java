package com.example.gastos.service;

import com.example.gastos.model.Debt;
import com.example.gastos.repository.DebtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DebtService {

    @Autowired
    private DebtRepository debtRepository;

    public void sendPaidNotification(Long debtId) {
        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new RuntimeException("Debt not found"));
        debt.setPaidNotificationSent(true);
        debtRepository.save(debt);
    }

    public void confirmPayment(Long debtId) {
        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new RuntimeException("Debt not found"));
        debt.setSettled(true);
        debtRepository.save(debt);
    }
}
