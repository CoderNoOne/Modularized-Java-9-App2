package converters.json.generator;

import converters.json.OrderJsonConverter;
import model.Category;
import model.Customer;
import model.OrderDTO;
import model.Product;
import net.andreinc.mockneat.MockNeat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class DataGenerator {

  private static MockNeat mockNeat = MockNeat.threadLocal();
  private static Random rnd = new Random();
  private static List<String> emails = emailsRepository();
  private static List<String> firstNames = firstnamesRepository();
  private static List<String> lastNames = lastnamesRepository();
  private static List<String> dates = datesRepository();


  public static void generate(final String jsonFilename) {
    new OrderJsonConverter(jsonFilename).toJson(generateOrders());
  }

  private DataGenerator() {
  }


  private static List<String> datesRepository() {

    return mockNeat
            .localDates()
            .between(LocalDate.now().minusYears(1), LocalDate.now().plusYears(5))
            .list(400).get()
            .stream().map(LocalDate::toString).collect(Collectors.toList());
  }

  private static Set<Customer> customerRepository() {
    return mockNeat
            .reflect(Customer.class)
            .field("name", mockNeat.from(firstNames))
            .field("surname", mockNeat.from(lastNames))
            .field("age", mockNeat.ints().rangeClosed(0, 80))
            .set(HashSet::new, 100)
            .val().stream().peek(customer -> customer.setEmail(getUniqueEmail()))
            .collect(Collectors.toSet());
  }

  private static List<OrderDTO> generateOrders() {

    return mockNeat
            .filler(OrderDTO::new)
            .setter(OrderDTO::setCustomer, mockNeat.from(new ArrayList<>(customerRepository())))
            .setter(OrderDTO::setProduct, mockNeat.from(productsRepository()))
            .setter(OrderDTO::setQuantity, mockNeat.ints().rangeClosed(0, 100))
            .setter(OrderDTO::setOrderDate, mockNeat.from(dates))
            .list(ArrayList::new, 1000)
            .val();
  }

  private static List<String> firstnamesRepository() {

    return mockNeat.names().first()
            .stream()
            .get()
            .limit(100)
            .map(first -> rnd.nextInt(50) > 10 ? first.toUpperCase() : first)
            .collect(Collectors.toList());
  }

  private static List<String> lastnamesRepository() {

    return mockNeat.names().last()
            .stream()
            .get()
            .limit(100)
            .map(last -> rnd.nextInt(50) < 40 ? last.toUpperCase() : last)
            .collect(Collectors.toList());
  }

  private static List<String> emailsRepository() {

    return mockNeat.emails()
            .stream()
            .get()
            .limit(500)
            .map(string -> rnd.nextInt(100) > 30 ? string : string.substring(0, string.indexOf('@') + 1))
            .collect(Collectors.toList());
  }

  private static String getUniqueEmail() {
    int index = rnd.nextInt(emails.size());
    String email = emails.get(index);
    emails.remove(index);
    return email;
  }

  private static List<Product> productsRepository() {
    return Arrays.asList(
            new Product(rnd.nextInt(3) % 2 == 0 ? "COMPUTER" : "computer", Category.ELECTRONICS, BigDecimal.valueOf(5_000)),
            new Product(rnd.nextInt(20) > 13 ? "HEADPHONES" : "headphones", Category.ELECTRONICS, BigDecimal.valueOf(300)),
            new Product("TV", Category.ELECTRONICS, BigDecimal.valueOf(10_000)),
            new Product(rnd.nextInt(4) < 2 ? "VR" : "vr", Category.ELECTRONICS, BigDecimal.valueOf(1_000)),
//            new Product("");
            null
    );
  }


}
