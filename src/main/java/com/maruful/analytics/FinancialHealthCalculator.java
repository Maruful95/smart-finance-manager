package com.maruful.analytics;

public class FinancialHealthCalculator {

  public int calculateScore(double income, double expense) {
    if (income == 0) return 0;

    double savings = income - expense;
    double savingsRatio = savings / income;
    double expenseRatio = expense / income;

    int score = 0;

    if (savingsRatio >= 0.5) {
      score += 50;
    } else if (savingsRatio >= 0.3) {
      score += 40;
    } else if (savingsRatio >= 0.2) {
      score += 30;
    } else if (savingsRatio > 0) {
      score += 20;
    } else {
      score += 0;
    }

    if (expenseRatio <= 0.5) {
      score += 30;
    } else if (expenseRatio <= 0.7) {
      score += 20;
    } else if (expenseRatio <= 0.9) {
      score += 10;
    } else {
      score += 0;
    }

    if (savings > 0) {
      score += 20;
    }
    return score;
  }

  public String getScoreLabel(int score) {
    if (score >= 80) return "Excellent";
    if (score >= 60) return "Good";
    if (score >= 40) return "Average";
    if (score >= 20) return "Poor";
    return "Critical";
  }
}
