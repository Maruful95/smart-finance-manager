package com.maruful.analytics;

public class FinancialHealthCalculator {

  public int calculateScore(double income, double expense) {
    if (income == 0) {
      return 0;
    }
    double savings = income - expense;
    double savingsRatio = (savings / income) * 100;
    double expenseRatio = (expense / income) * 100;

    int score = 0;

    if (savingsRatio >= 20) {
      score += 40;
    } else {
      score += (int) (savingsRatio * 2);
    }
    if (expenseRatio <= 50) {
      score += 30;
    } else {
      score += (int) ((100 - expenseRatio) * 0.3);
    }
    if (savings > 0) {
      score += 30;
    } else {
      score += 10;
    }
    return score;
  }
}
