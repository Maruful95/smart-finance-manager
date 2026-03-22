package com.maruful.recommendation;

public class Recommendation {

    public String generateRecommendation(double income, double expense, int score) {

        if (income == 0) {
            return "No income. Start adding income sources.";
        }

        double savings = income - expense;
        double savingsRatio = (savings / income) * 100;
        double expenseRatio = (expense / income) * 100;

        if (expenseRatio > 80) {
            return "Warning: Your expenses exceed 80% of your income.";
        }

        if (savingsRatio < 20) {
            return "Suggestion: Try to save at least 20% of your income.";
        }

        if (score >= 80) {
            return "Excellent financial condition. Keep it up!";
        }

        if (score >= 60) {
            return "Good financial condition. Some improvements possible.";
        }

        if (score >= 40) {
            return "Average condition. Control expenses and increase savings.";
        }

        return "Poor financial condition. Immediate action required!";
    }
}
