package service;


import converters.others.OrderServiceConverter;
import exceptions.AppException;
import model.*;
import org.eclipse.collections.impl.collector.Collectors2;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;


public class OrdersService {

  private List<Order> orders;

  public OrdersService(final String filename) {

    orders = getOrdersFromJson(filename);
  }

  private List<Order> getOrdersFromJson(final String jsonFilename) {

   /* if (jsonFilename == null || !jsonFilename.matches("[\\w]+\\.json")) {
      throw new AppException("JSON FILE IS NOT CORRECT");
    }*/

    return new OrderServiceConverter().toOrderList(jsonFilename);
  }

  public OrdersService() {

  }

  private static Customer createCustomer() {

    System.out.println("INPUT CUSTOMER INFO DETAIL");
    Customer.CustomerBuilder builder = null;

    try (Scanner sc = new Scanner(System.in)) {

      System.out.println("INPUT CUSTOMER FIRST NAME");
      builder = Customer.CustomerBuilder.aCustomer().name(sc.nextLine());
      System.out.println("INPUT CUSTOMER LAST  NAME");
      builder = builder.surname(sc.nextLine());
      System.out.println("INPUT CUSTOMER AGE");
      builder = builder.age(sc.nextInt());
      System.out.println("INPUT CUSTOMER EMAIL");
      builder = builder.email(sc.nextLine());

    } catch (InputMismatchException e) {
      System.err.println(Arrays.toString(e.getStackTrace()));
      throw new AppException("YOU 'VE INPUTTED BAD DATA TYPE");
    }

    return builder.build();
  }

  private static Product createProduct() {
    System.out.println("INPUT PRODUCT INFO DETAIL");
    Product.ProductBuilder builder = null;

    try (Scanner sc = new Scanner(System.in)) {

      System.out.println("INPUT PRODCUT NAME");
      builder = Product.ProductBuilder.aProduct().name(sc.nextLine());
      System.out.println("INPUT PRODUCT CATEGORY");
      builder = builder.name(sc.nextLine().toUpperCase());
      System.out.println("INPUT PRODUCT PRICE");
      builder = builder.price(sc.nextBigDecimal());

    } catch (InputMismatchException e) {
      System.err.println(Arrays.toString(e.getStackTrace()));
      throw new AppException("YOU 'VE INPUTTED BAD DATA TYPE");
    }

    return builder.build();

  }

  /*private static OrderDTO createOrder() {

    System.out.println("INPUT ORDER DETAIL");
    Customer customer = createCustomer();
    Product product = createProduct();

    try (Scanner sc = new Scanner(System.in)) {
      System.out.println("INPUT ORDER QUANTITY");
      int quantity = sc.nextInt();
      System.out.println("INPUT ORDER DATE");
      String stringDate = sc.nextLine();
      if (!isDateValid(stringDate)) {
        throw new AppException("");
      }

    } catch () {

    }
  }

  public static boolean isDateValid(String date) {

    try {
      DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
      df.setLenient(false);
      df.parse(date)

    } catch (ParseException e) {
      System.err.println(Arrays.toString(e.getStackTrace()));
      throw new AppException("");
    }
  }*/


  public BigDecimal averageProductsPriceBoughtWithinDateRange(LocalDate minDate, LocalDate maxDate) {

    return orders.
            stream()
            .filter(order -> order.getOrderDate().compareTo(minDate) >= 0 && order.getOrderDate().compareTo(maxDate) <= 0)
            .collect(Collectors2.summarizingBigDecimal(order -> order.getProduct().getPrice()))
            .getAverage(new MathContext(2, RoundingMode.HALF_UP));

  }

  public Map<Category, Product> mostExpensiveProductInEachCategory() {

    return orders
            .stream()
            .map(order -> order.getProduct().getCategory())
            .distinct()
            .collect(Collectors.toMap(
                    category -> category,
                    category -> orders.stream().filter(order -> order.getProduct().getCategory().equals(category))
                            .max(Comparator.comparing(Order::getQuantity)).get().getProduct()));

  }


  public Map<String, LocalDate> dateWithMostAndLeastPurchases() {

    return orders
            .stream()
            .collect(Collectors.collectingAndThen(Collectors.groupingBy(Order::getOrderDate, Collectors.summingInt(Order::getQuantity)),
                    map -> {
                      LinkedList<LocalDate> collect = map.entrySet().stream()
                              .sorted(Map.Entry.comparingByValue())
                              .map(Map.Entry::getKey)
                              .collect(Collectors.toCollection(LinkedList::new));
                      return Map.of("Min", collect.getFirst(), "Max", collect.getLast());
                    }));

  }

  public Customer customerWhoPaidTheMostForAllPurchases() {

    return orders
            .stream()
            .collect(Collectors.collectingAndThen(Collectors.groupingBy(Order::getCustomer,
                    Collectors2.summingBigDecimal(order -> BigDecimal.valueOf(order.getQuantity()).multiply(order.getProduct().getPrice()))),
                    map -> map.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey()));
  }

  public Map<Customer, List<Product>> productsListForCustomers() {

    return orders
            .stream()
            .collect(Collectors.groupingBy(Order::getCustomer, Collectors.mapping(Order::getProduct, Collectors.toList())));

    // dorobic wysłanie maila

  }


  public int numberOfCustomersWhoBoughtAtLeastXProductsEachTime(int x) {

    return orders
            .stream()
            .filter(order -> order.getQuantity() > x)
            .map(Order::getCustomer)
            .distinct()
            .mapToInt(e -> 1)
            .sum();
  }


  public Map<Month, Long> numberOfProductsOrderedByMonth() {

    return orders
            .stream()
            .collect(Collectors.groupingBy(order -> order.getOrderDate().getMonth(),
                    Collectors.mapping(Order::getProduct, Collectors.counting())));

  }

  public Map<Month, Category> mostPopularProductCategoryByOrderMonth() {

    return orders
            .stream()
            .map(order -> order.getOrderDate().getMonth())
            .distinct()
            .collect(Collectors.toMap(
                    month -> month,
                    month -> orders.stream().filter(order -> order.getOrderDate().getMonth().equals(month)).collect(Collectors.groupingBy(order -> order.getProduct().getCategory(),
                            Collectors.counting())).entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey()));
  }


  public Category mostPopularProductCategory() {

    return orders.stream()
            .collect(Collectors.groupingBy(
                    e -> e.getProduct().getCategory(),
                    Collectors.summingInt(Order::getQuantity)))
            .entrySet().stream().max(Map.Entry.comparingByValue())
            .orElseThrow(() -> new AppException("No category found"))
            .getKey();

  }

  public BigDecimal totalPriceForALlPurchasesAfterDiscount() {


    return new ArrayList<>(orders)
            .stream()
            .peek(order -> {
              if (order.getCustomer().getAge() < 25) {
                order.getProduct().setPrice(BigDecimal.valueOf(0.97).multiply(order.getProduct().getPrice()));
              } else if (order.getOrderDate().compareTo(LocalDate.now().minusDays(2)) <= 0) {
                order.getProduct().setPrice(BigDecimal.valueOf(0.98).multiply(order.getProduct().getPrice()));
              }
            }).map(order -> order.getProduct().getPrice().multiply(BigDecimal.valueOf(order.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

  }

}
