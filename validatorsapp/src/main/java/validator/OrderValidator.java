package validator;


import model.Order;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class OrderValidator implements Validator <Order>{

  private Map<String, String> errors = new HashMap<>();
  private ProductValidator productValidator = new ProductValidator();
  private CustomerValidator customerValidator = new CustomerValidator();

  public Map<String, String> validate(Order order) {
    errors.clear();

    if (order == null) {
      errors.put("order", "order object is null");
      return errors;
    }

    if (order.getProduct() == null) {
      errors.put("Product", "product object is null");
    } else {
      Map<String, String> productErrors = productValidator.validate(order.getProduct());
      errors.putAll(productErrors);
    }

    if (order.getCustomer() == null) {
      errors.put("customer", "customer object is null");
    } else {

      Map<String, String> customerErrors = customerValidator.validate(order.getCustomer());
      errors.putAll(customerErrors);
    }


    if (!isQuantityValid(order)) {
      errors.put("product quantity", "product quantity must be number greater than 0");
    }

    if (!isOrderDateValid(order)) {
      errors.put("orderDate", "OrderDate cannot be done in the past");
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
