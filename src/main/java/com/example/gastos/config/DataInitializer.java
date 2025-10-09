package com.example.gastos.config;

import com.example.gastos.model.Group;
import com.example.gastos.model.User;
import com.example.gastos.service.ExpenseService;
import com.example.gastos.service.GroupService;
import com.example.gastos.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private ExpenseService expenseService;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (userService.findByUsername("admin_user").isPresent()) {
            return;
        }

        // 1. Create Users
        User adminUser = userService.createUser("admin_user", "password", "admin@test.com");
        User member1 = userService.createUser("member_user1", "password", "member1@test.com");
        User member2 = userService.createUser("member_user2", "password", "member2@test.com");

        // 2. Create Group 1 (admin_user is admin)
        Group beachTrip = groupService.createGroup("Viaje a la Playa", "Gastos del viaje a la playa de fin de semana.", adminUser);
        groupService.addMember(beachTrip, member1, false);
        groupService.addMember(beachTrip, member2, false);

        // Add expenses to Group 1
        expenseService.addExpense(beachTrip, adminUser, "Gasolina", new BigDecimal("50.00"));
        expenseService.addExpense(beachTrip, member1, "Alojamiento", new BigDecimal("100.00"));
        expenseService.addExpense(beachTrip, member2, "Bebidas", new BigDecimal("30.00"));

        // 3. Create Group 2 (admin_user is just a member)
        Group weeklyDinner = groupService.createGroup("Cena Semanal", "Pizzas de los viernes.", member1);
        groupService.addMember(weeklyDinner, adminUser, false);

        // Add expenses to Group 2
        expenseService.addExpense(weeklyDinner, member1, "Pizzas", new BigDecimal("60.00"));
    }
}
