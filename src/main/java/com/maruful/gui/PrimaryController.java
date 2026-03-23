package com.maruful.gui;

import com.maruful.analytics.*;
import com.maruful.data.DataHandler;
import com.maruful.manager.FinanceManager;
import com.maruful.model.Transaction;
import com.maruful.recommendation.Recommendation;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
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
  private Label savingsRatioLabel;

  @FXML
  private Label expenseRatioLabel;

  @FXML
  private TableColumn<Transaction, Void> actionCol;

  @FXML
  private PieChart expenseChart;

  @FXML
  private BarChart<String, Number> barChart;

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

    actionCol.setCellFactory(col ->
      new TableCell<>() {
        private final Button deleteButton = new Button("X");

        {
          deleteButton.setOnAction(event -> {
            Transaction t = getTableView().getItems().get(getIndex());

            manager.getTransactions().remove(t);

            try {
              DataHandler.save(manager.getTransactions());
            } catch (Exception e) {
              e.printStackTrace();
            }

            updateUI();
          });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
          super.updateItem(item, empty);

          if (empty) {
            setGraphic(null);
          } else {
            setGraphic(deleteButton);
          }
        }
      }
    );

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

      double amount;
      try {
        amount = Double.parseDouble(amountText);
      } catch (NumberFormatException e) {
        System.out.println("Invalid amount! Please enter a valid number.");
        return;
      }

      Transaction t = new Transaction(type, category, amount, date, desc);
      manager.addTransaction(t);

      System.out.println("Saving transaction...");
      DataHandler.save(manager.getTransactions());

      typeBox.setValue(null);
      categoryField.clear();
      amountField.clear();
      dateField.clear();
      descField.clear();

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
    double savingsRatio = engine.calculateSavingsRatio(income, expense);
    double expenseRatio = engine.calculateExpenseRatio(income, expense);

    incomeLabel.setText("Income: " + income);
    expenseLabel.setText("Expense: " + expense);
    balanceLabel.setText("Balance: " + manager.getBalance());
    scoreLabel.setText("Score: " + score);
    recommendationLabel.setText("Recommendation: " + advice);
    savingsRatioLabel.setText(
      String.format("Savings Ratio: %.2f%%", savingsRatio)
    );
    expenseRatioLabel.setText(
      String.format("Expense Ratio: %.2f%%", expenseRatio)
    );

    tableView.getItems().clear();
    tableView.getItems().addAll(manager.getTransactions());
    tableView.refresh();

    System.out.println("Transactions: " + manager.getTransactions().size());

    // chart
    expenseChart.getData().clear();

    Map<String, Double> categoryMap = new HashMap<>();

    for (Transaction t : manager.getTransactions()) {
      if (t.getType().equalsIgnoreCase("expense")) {
        categoryMap.put(
          t.getCategory(),
          categoryMap.getOrDefault(t.getCategory(), 0.0) + t.getAmount()
        );
      }
    }
    if (!categoryMap.isEmpty()) {
      for (String category : categoryMap.keySet()) {
        expenseChart
          .getData()
          .add(new PieChart.Data(category, categoryMap.get(category)));
      }
    }

    barChart.getData().clear();

    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName("Overview");
    series.getData().add(new XYChart.Data<>("Income", income));
    series.getData().add(new XYChart.Data<>("Expense", expense));
    barChart.getData().add(series);

    expenseChart.setAnimated(true);
    barChart.setAnimated(true);
    expenseChart.layout();
    barChart.layout();

    tableView.refresh();
  }
}
