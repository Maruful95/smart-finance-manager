package com.maruful.gui;

import java.io.IOException;
import javafx.fxml.FXML;
import com.maruful.App;
import com.maruful.manager.FinanceManager;
import com.maruful.model.Transaction;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {

        FinanceManager manager = new FinanceManager();
        
        manager.addTransaction(
            new Transaction("income", "Salary", 20000, "2026-03-18", "Monthly Salary")
        );
        
        manager.addTransaction(
            new Transaction("expense", "Food", 500, "2026-03-18", "Lunch")
        );
        
        System.out.println("Balance: " + manager.getBalance());
    }
}