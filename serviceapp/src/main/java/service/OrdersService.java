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

import static j2html.TagCreator.*;


public class OrdersService {

  private List<Order> orders;

  public OrdersService(final String filename) {

    orders = getOrdersFromJson(filename);
  }

  private List<Order> getOrdersFromJson(final String jsonFilename) {
    return new OrderServiceConverter().toOrderList(jsonFilename);
  }

  public OrdersService() {
  }

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

    Map<Customer, List<Product>> customerProductMap = orders
            .stream()
            .collect(Collectors.groupingBy(Order::getCustomer, Collectors.mapping(Order::getProduct, Collectors.toList())));

    customerProductMap.
            forEach((customer, productList) ->
                    EmailService.sendAsHtml(customer.getEmail(), "Order info from app", clientInfo(customer)
                            + productList.stream().map(OrdersService::productInfo).collect(Collectors.joining())));

    return customerProductMap;
  }

  private static String clientInfo(Customer key) {

    return h3("Client info:").render() +
            table().with(
                    tr().with(
                            th().withText("First Name"),
                            th().withText("Last Name"),
                            th().withText("Age")
                    ),
                    tr().with(
                            td().withText(key.getName()),
                            td().withText(key.getSurname()),
                            td().withText(String.valueOf(key.getAge()))
                    )
            ).render() + hr().render();
  }

  private static String productInfo(Product product) {

    return h3("Product info: ").render() +
            table().with(
                    tr().with(
                            th().withText("Name"),
                            th().withText("Category"),
                            th().withText("Price")
                    ),
                    tr().with(
                            td().withText(product.getName()),
                            td().withText(product.getCategory().toString()),
                            td().withText(product.getPrice().toString())
                    )
            ).render() + hr().render();
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

  public Map<Month, Map.Entry<Category, Long>> mostPopularProductCategoryByOrderMonth() {

    return orders
            .stream()
            .map(order -> order.getOrderDate().getMonth())
            .distinct()
            .collect(Collectors.toMap(
                    month -> month,
                    month -> orders.stream().filter(order -> order.getOrderDate().getMonth().equals(month)).collect(Collectors.groupingBy(order -> order.getProduct().getCategory(),
                            Collectors.counting())).entrySet().stream().max(Map.Entry.comparingByValue()).get()));
  }

  public Map<Category, Integer> mostPopularProductCategory() {

    return orders.stream()
            .collect(Collectors.collectingAndThen(Collectors.groupingBy(
                    e -> e.getProduct().getCategory(), Collectors.summingInt(Order::getQuantity)),
                    map -> {
                      Integer max = map.entrySet().stream().max(Comparator.comparing(Map.Entry::getValue))
                              .orElseThrow(() -> new AppException("No entry")).getValue();

                      return map
                              .entrySet()
                              .stream()
                              .filter(e -> e.getValue().equals(max))
                              .collect(Collectors.toMap(
                                      Map.Entry::getKey,
                                      Map.Entry::getValue));
                    }));
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
