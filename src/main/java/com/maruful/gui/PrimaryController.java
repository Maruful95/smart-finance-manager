package com.maruful.gui;

import java.io.IOException;
import javafx.fxml.FXML;
import com.maruful.manager.FinanceManager;
import com.maruful.model.Transaction;
import com.maruful.analytics.AnalyticsEngine;
import com.maruful.analytics.FinancialHealthCalculator;
import com.maruful.recommendation.Recommendation;

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
        
        double income = manager.getTotalIncome();
        double expense = manager.getTotalExpense();

        AnalyticsEngine engine = new AnalyticsEngine();
        FinancialHealthCalculator calculator = new FinancialHealthCalculator();
        int score = calculator.calculateScore(income, expense);
        Recommendation recommend = new Recommendation();
        String advice = recommend.generateRecommendation(income, expense, score);

        System.out.println("Balance: " + manager.getBalance());
        System.out.println("Savings Ratio: " + engine.calculateSavingsRatio(income, expense));
        System.out.println("Expense Ratio: " + engine.calculateExpenseRatio(income, expense));
        System.out.println("Financial Health Score: " + score);
        System.out.println("Recommendation: " + advice);
    }
}