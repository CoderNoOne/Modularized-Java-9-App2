package main;

import converters.json.generator.DataGenerator;
import exceptions.AppException;
import service.OrdersService;
import service.UserDataService;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Arrays;

public class Menu {
  private final OrdersService ordersService;
  private final UserDataService userDataService = new UserDataService();

  public Menu(final String jsonFilename) {

    if (jsonFilename == null || !jsonFilename.matches(".+\\.json$")) {
      throw new AppException("WRONG JSON FILE FORMAT");
    }
    DataGenerator.generate(jsonFilename);
    ordersService = new OrdersService(jsonFilename);
  }

  public void mainMenu() {
    menuOptions();
    while (true) {
      try {
        int option = userDataService.getInt("INPUT YOUR OPTION: ");
        switch (option) {
          case 1:
            option1();
            break;
          case 2:
            option2();
            break;
          case 3:
            option3();
            break;
          case 4:
            option4();
            break;
          case 5:
            option5();
            break;
          case 6:
            option6();
            break;
          case 7:
            option7();
            break;
          case 8:
            option8();
            break;
          case 9:
            option9();
            break;
          case 10:
            option10();
            break;
          case 11:
            userDataService.close();
            return;
          case 12:
            menuOptions();
            break;
          default:
            throw new AppException("INPUT OPTION IS NOT DEFINED");
        }

      } catch (AppException e) {
        System.out.println(e.getExceptionMessage());
        System.err.println(Arrays.toString(e.getStackTrace()));
      }
    }
  }

  private void option1() {
    LocalDate minDate = userDataService.getDate("Input min date");
    LocalDate maxDate = userDataService.getDate("Input max date");

    if (minDate.compareTo(maxDate) >= 0) {
      throw new AppException("Max date must be greater than min date");
    }

    System.out.println("AVERAGE PRODUCT PRIZE BOUGHT WITHIND DATES: " +
            minDate + " and " + maxDate + ": " + ordersService.averageProductsPriceBoughtWithinDateRange(minDate, maxDate));
  }

  private void option2() {
    ordersService.mostExpensiveProductInEachCategory().forEach(
            (category, product) -> System.out.println("Category: " + category +
                    " -> " + "Most expensive product: " + product));
  }

  private void option3() {
    ordersService.productsListForCustomers().forEach(
            (customer, products) -> System.out.println("Customer: " + customer
                    + " -> " + products + "\n"));
  }

  private void option4() {
    ordersService.dateWithMostAndLeastPurchases().forEach(
            (key, date) -> System.out.println(key + " -> " + date));
  }

  private void option5() {
    System.out.println(ordersService.customerWhoPaidTheMostForAllPurchases());
  }

  private void option6() {
    System.out.println(ordersService.totalPriceForALlPurchasesAfterDiscount());
  }

  private void option7() {
    int minDealAmount = userDataService.getInt("INPUT MIN DEAL DONE BY EACH CLIENT");
    if (minDealAmount <= 0) {
      throw new AppException("PRODUCT QUANTITY MUST BE GREATER THAN 0!");
    }
    System.out.println("NUMBER OF CUSTOMERS WHO BOUGHT EACH TIME AT LEAST X NUMBER OF PRODUCTS:  "
            + ordersService.numberOfCustomersWhoBoughtAtLeastXProductsEachTime(minDealAmount));
  }

  private void option8() {
    System.out.println("MOST POPULAR PRODUCT CATEGORY: " + ordersService.mostPopularProductCategory());
  }

  private void option9() {
    ordersService.numberOfProductsOrderedByMonth().forEach(
            (month, amount) -> System.out.println("MONTH: " + month
                    + " -> " + "AMOUNT OF PRODUCT BOUGHT: " + amount));
  }

  private void option10() {
    ordersService.mostPopularProductCategoryByOrderMonth().forEach(
            (month, category) -> System.out.println("MONTH: " + month
                    + " -> " + "MOST POPULAR CATEGORY: " + category));
  }

  private static void menuOptions() {

    String menu = MessageFormat.format(
            "\nOption no. 1 - {0}\n" +
                    "Option no. 2 - {1}\n" +
                    "Option no. 3 - {2}\n" +
                    "Option no. 4 - {3}\n" +
                    "Option no. 5 - {4}\n" +
                    "Option no. 6 - {5}\n" +
                    "Option no. 7 - {6}\n" +
                    "Option no. 8 - {7}\n" +
                    "Option no. 9 - {8}\n" +
                    "Option no. 10 - {9}\n" +
                    "Option no. 11 - {10}\n" +
                    "Option no. 12 - {11}",

            "AVERAGE PRODUCTS PRICE BOUGHT WITHIN DATE RANGE",
            "MOST EXPENSIVE PRODUCT IN EACH PRODUCT CATEGORY",
            "PRODUCT LIST FOR CUSTOMERS",
            "DATE WITH MOST AND LEAST PURCHASES DONE",
            "CUSTOMER WHO PAID THE MOST FOR ALL PURCHASES",
            "TOTAL PRICE FOR ALL PURCHASES AFTER DISCOUNT",
            "AMOUNT OF CUSTOMERS WHO BOUGHT EACH TIME AT LEAST INPUT NUMBER OF PRODUCT",
            "MOST POPULAR PRODUCT CATEGORY",
            "AMOUNT OF PRODUCT BOUGHT GROUPED BY ORDER MONTH",
            "MOST POPULAR PRODUCT CATEGORY GROUPED BY ORDER MONTH",
            "EXIT THE PROGRAM",
            "SHOW MENU OPTIONS"
    );
    System.out.println(menu);
  }
}