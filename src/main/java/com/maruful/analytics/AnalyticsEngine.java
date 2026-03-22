package com.maruful.analytics;

public class AnalyticsEngine {

  public double calculateSavingsRatio(double income, double expense) {
    if (income == 0) {
      return 0;
    }
    double savings = income - expense;

    return (savings / income) * 100;
  }

  public double calculateExpenseRatio(double income, double expense) {
    if (income == 0) {
      return 0;
    }
    return (expense / income) * 100;
  }
}
