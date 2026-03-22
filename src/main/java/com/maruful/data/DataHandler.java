package com.maruful.data;

import com.maruful.model.Transaction;
import java.io.*;
import java.util.*;

public class DataHandler {

  public static void save(List<Transaction> list) throws Exception {
    File file = new File("data.csv");
    System.out.println("Saving to: " + file.getAbsolutePath());

    PrintWriter writer = new PrintWriter(file);

    for (Transaction t : list) {
      writer.println(
        t.getType() +
          "," +
          t.getCategory() +
          "," +
          t.getAmount() +
          "," +
          t.getDate() +
          "," +
          t.getDescription()
      );
    }

    writer.close();
  }

  public static List<Transaction> load() throws Exception {
    File file = new File("data.csv");
    System.out.println("Loading from: " + file.getAbsolutePath());

    List<Transaction> list = new ArrayList<>();

    if (!file.exists()) {
      System.out.println("File not found!");
      return list;
    }

    Scanner sc = new Scanner(file);

    while (sc.hasNextLine()) {
      String line = sc.nextLine();
      System.out.println("Read line: " + line);

      String[] data = line.split(",",-1);

      list.add(
        new Transaction(
          data[0],
          data[1],
          Double.parseDouble(data[2]),
          data[3],
          data[4]
        )
      );
    }

    sc.close();
    return list;
  }
}
