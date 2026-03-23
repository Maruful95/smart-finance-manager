package com.maruful.manager;

import com.maruful.model.Transaction;
import java.util.ArrayList;

public class FinanceManager {

  private ArrayList<Transaction> transactions = new ArrayList<>();

  public void addTransaction(Transaction t) {
    transactions.add(t);
  }

  public double getTotalIncome() {
    double total = 0;

    for (Transaction t : transactions) {
      if (t.getType().equalsIgnoreCase("income")) {
        total += t.getAmount();
      }
    }
    return total;
  }

  public double getTotalExpense() {
    double total = 0;

    for (Transaction t : transactions) {
      if (t.getType().equalsIgnoreCase("expense")) {
        total += t.getAmount();
      }
    }
    return total;
  }

  public double getBalance() {
    return getTotalIncome() - getTotalExpense();
  }

  public ArrayList<Transaction> getTransactions() {
    return transactions;
  }
}
