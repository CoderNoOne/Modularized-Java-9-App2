package service;

import exceptions.AppException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Scanner;

public class UserDataService {

  private Scanner sc = new Scanner(System.in);

  public int getInt(String message) {
    System.out.println(message);

    String text = sc.nextLine();
    if (!text.matches("[\\d]+")) {
      throw new AppException(("INT VALUE IS NOT CORRECT: " + text));
    }

    return Integer.parseInt(text);
  }

  public void close() {
    if (sc != null) {
      sc.close();
      sc = null;
    }
  }

  public LocalDate getDate(String message) {
    System.out.println(message);

    String date = sc.nextLine();
    LocalDate localDate;

    try {
      localDate = LocalDate.parse(date);
    } catch (DateTimeParseException e) {
      System.err.println(Arrays.toString(e.getStackTrace()));
      throw new AppException("Date input was of bad format");
    }

    return localDate;
  }
}
