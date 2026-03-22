package com.maruful.gui;

import com.maruful.analytics.*;
import com.maruful.data.DataHandler;
import com.maruful.manager.FinanceManager;
import com.maruful.model.Transaction;
import com.maruful.recommendation.Recommendation;
import javafx.beans.property.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PrimaryController {

  private FinanceManager manager = new FinanceManager();

  @FXML
  private ComboBox<String> typeBox;

  @FXML
  private TextField categoryField;

  @FXML
  private TextField amountField;

  @FXML
  private TextField dateField;

  @FXML
  private TextField descField;

  @FXML
  private Label incomeLabel;

  @FXML
  private Label expenseLabel;

  @FXML
  private Label balanceLabel;

  @FXML
  private Label scoreLabel;

  @FXML
  private Label recommendationLabel;

  @FXML
  private TableView<Transaction> tableView;

  @FXML
  private TableColumn<Transaction, String> dateCol;

  @FXML
  private TableColumn<Transaction, String> categoryCol;

  @FXML
  private TableColumn<Transaction, String> typeCol;

  @FXML
  private TableColumn<Transaction, Double> amountCol;

  @FXML
  private TableColumn<Transaction, String> descriptionCol;

  @FXML
  public void initialize() {
    typeBox.getItems().addAll("Income", "Expense");

    dateCol.setCellValueFactory(cellData ->
      new SimpleStringProperty(cellData.getValue().getDate())
    );
    categoryCol.setCellValueFactory(cellData ->
      new SimpleStringProperty(cellData.getValue().getCategory())
    );
    typeCol.setCellValueFactory(cellData ->
      new SimpleStringProperty(cellData.getValue().getType())
    );
    amountCol.setCellValueFactory(cellData ->
      new SimpleObjectProperty<>(cellData.getValue().getAmount())
      );
    descriptionCol.setCellValueFactory(cellData ->
      new SimpleStringProperty(cellData.getValue().getDescription())
    );

    try {
      manager.getTransactions().addAll(DataHandler.load());
    } catch (Exception e) {
      System.out.println("No previous data");
    }
    updateUI();
  }

  @FXML
  private void handleAddTransaction() {
    try {
      String type = typeBox.getValue();
      String category = categoryField.getText();
      String amountText = amountField.getText();
      String date = dateField.getText();
      String desc = descField.getText();

      if (
        type == null ||
        category.isEmpty() ||
        amountText.isEmpty() ||
        date.isEmpty()
      ) {
        System.out.println("Please fill all required fields!");
        return;
      }

      double amount = Double.parseDouble(amountText);
      Transaction t = new Transaction(type, category, amount, date, desc);
      manager.addTransaction(t);

      System.out.println("Saving transaction...");
      DataHandler.save(manager.getTransactions());

      updateUI();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void updateUI() {
    double income = manager.getTotalIncome();
    double expense = manager.getTotalExpense();

    AnalyticsEngine engine = new AnalyticsEngine();
    FinancialHealthCalculator calculator = new FinancialHealthCalculator();
    Recommendation recommend = new Recommendation();

    int score = calculator.calculateScore(income, expense);
    String advice = recommend.generateRecommendation(income, expense, score);

    incomeLabel.setText("Income: " + income);
    expenseLabel.setText("Expense: " + expense);
    balanceLabel.setText("Balance: " + manager.getBalance());
    scoreLabel.setText("Score: " + score);
    recommendationLabel.setText("Recommendation: " + advice);

    tableView.getItems().clear();
    tableView.getItems().addAll(manager.getTransactions());
    tableView.refresh();

    System.out.println("Transactions: " + manager.getTransactions().size());
  }
}
