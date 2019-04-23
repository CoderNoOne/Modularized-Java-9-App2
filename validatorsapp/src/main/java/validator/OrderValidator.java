package validator;


import model.Order;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class OrderValidator implements Validator<Order> {

  private Map<String, String> errors = new HashMap<>();
  private ProductValidator productValidator = new ProductValidator();
  private CustomerValidator customerValidator = new CustomerValidator();

  public Map<String, String> validate(Order order) {
    errors.clear();

    if (order == null) {
      errors.put("Order", "Order object is null");
      return errors;
    }

    if (order.getProduct() == null) {
      errors.put("Product", "Product object is null");
    } else {
      Map<String, String> productErrors = productValidator.validate(order.getProduct());
      errors.putAll(productErrors);
    }

    if (order.getCustomer() == null) {
      errors.put("Customer", "Customer object is null");
    } else {

      Map<String, String> customerErrors = customerValidator.validate(order.getCustomer());
      errors.putAll(customerErrors);
    }


    if (!isQuantityValid(order)) {
      errors.put("Product quantity", "Product quantity must be number greater than 0");
    }

    if (!isOrderDateValid(order)) {
      errors.put("OrderDate", "Date order should take place in the future");
    }

    return errors;
  }

  private boolean isOrderDateValid(Order order) {
    return order.getOrderDate().compareTo(LocalDate.now()) >= 0;
  }

  private boolean isQuantityValid(Order order) {
    return order.getQuantity() > 0;
  }

  public boolean hasErrors() {
    return !errors.isEmpty();
  }

}
