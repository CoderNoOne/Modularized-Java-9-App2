package converters.json.generator;

import converters.json.OrderDTOJsonConverter;
import exceptions.AppException;
import model.Category;
import model.Customer;
import model.OrderDTO;
import model.Product;
import net.andreinc.mockneat.unit.seq.Seq;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDate.now;
import static net.andreinc.mockneat.types.enums.StringFormatType.LOWER_CASE;
import static net.andreinc.mockneat.types.enums.StringFormatType.UPPER_CASE;
import static net.andreinc.mockneat.unit.objects.Filler.filler;
import static net.andreinc.mockneat.unit.objects.From.from;
import static net.andreinc.mockneat.unit.objects.Probabilities.probabilities;
import static net.andreinc.mockneat.unit.time.LocalDates.localDates;
import static net.andreinc.mockneat.unit.types.Bools.bools;
import static net.andreinc.mockneat.unit.types.Ints.ints;
import static net.andreinc.mockneat.unit.types.Longs.longs;
import static net.andreinc.mockneat.unit.user.Emails.emails;
import static net.andreinc.mockneat.unit.user.Names.names;


public class DataGenerator {

  public static final int NUM_DATES = 400;
  public static final int NUM_CUSTOMER = 100;
  public static final int NUM_ORDERS = 500;
  public static final int NUM_FNAMES = 100;
  public static final int NUM_LNAMES = 100;
  public static final int NUM_EMAILS = 500;
  public static final int NUM_PRODUCTS = 5;

  private static final Map<Category, List<String>> productNamesByCategory = new EnumMap<>(Category.class);
  private static final List<String> emails = emailsRepository();
  private static final List<String> firstNames = firstNamesRepository();
  private static final List<String> lastNames = lastNamesRepository();
  private static final List<String> dates = datesRepository();
  private static final String filename = "ProductsNameByCate5gory";

  static {

    try {
      productNamesByCategory.putAll(
              Files.readAllLines(Paths.get(".\\convertersapp\\" + filename))
                      .stream()
                      .map(line -> line.split("[=]"))
                      .filter(arr -> Arrays.stream(Category.values()).map(Category::toString).anyMatch(arr[0]::equals))
                      .collect(Collectors.groupingBy(arr -> Category.valueOf(arr[0]),
                              Collectors.flatMapping(arr -> Arrays.stream(arr[1].split("[,]")),
                                      Collectors.toList()))));
    } catch (IOException e) {
      System.out.println(e.getMessage());
      System.err.println(Arrays.toString(e.getStackTrace()));
      throw new AppException("Problem with " + filename);
    }
  }

  public static void generate(final String jsonFilename) {
    new OrderDTOJsonConverter(jsonFilename).toJson(generateOrders());
  }

  private DataGenerator() {
  }

  private static List<String> datesRepository() {

    return localDates()
            .between(now().minusYears(1), now().plusYears(5))
            .mapToString()
            .list(NUM_DATES)
            .get();
  }

  private static Set<Customer> customerRepository() {

    return filler(Customer::new)
            .setter(Customer::setName, from(firstNames))
            .setter(Customer::setSurname, from(lastNames))
            .setter(Customer::setAge, ints().rangeClosed(0, 80))
            .setter(Customer::setEmail, Seq.fromIterable(emails))
            .set(HashSet::new, NUM_CUSTOMER)
            .get();
  }

  private static List<OrderDTO> generateOrders() {

    return filler(OrderDTO::new)
            .setter(OrderDTO::setCustomer, from(new ArrayList<>(customerRepository())))
            .setter(OrderDTO::setProduct, from(productRepository()))
            .setter(OrderDTO::setQuantity, ints().rangeClosed(0, 5))
            .setter(OrderDTO::setOrderDate, from(dates))
            .list(ArrayList::new, NUM_ORDERS)
            .val();
  }

  private static List<String> firstNamesRepository() {

    return probabilities(String.class)
            .add(0.1, names().first().format(LOWER_CASE))
            .add(0.9, names().first().format(UPPER_CASE))
            .list(NUM_FNAMES)
            .get();
  }

  private static List<String> lastNamesRepository() {
    return probabilities(String.class)
            .add(0.1, names().last().format(LOWER_CASE))
            .add(0.9, names().last().format(UPPER_CASE))
            .list(NUM_LNAMES)
            .get();

  }

  private static List<String> emailsRepository() {

    return emails()
            .list(NUM_EMAILS)
            .get();
  }

  private static List<Product> productCategoryRepository(Category category, List<String> categoryProductNames) {

    return filler(Product::new)
            .setter(Product::setName,
                    Seq.fromIterable(categoryProductNames)
                            .cycle(true)
                            .map(p -> {
                              if (bools().probability(0.1).get()) {
                                p = p.toLowerCase();
                              }
                              return p;
                            })
            )
            .constant(Product::setCategory, category)
            .setter(Product::setPrice, longs().range(100, 500).map(BigDecimal::valueOf))
            .list(NUM_PRODUCTS)
            .get();
  }

  private static List<Product> productRepository() {
    return productNamesByCategory
            .entrySet()
            .stream()
            .flatMap(e -> productCategoryRepository(e.getKey(), e.getValue()).stream())
            .collect(Collectors.toList());
  }
}

