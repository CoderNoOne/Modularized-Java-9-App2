package validator;

import model.Product;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ProductValidator {
  private Map<String, String> errors = new HashMap<>();

  public Map<String, String> validate(Product product) {
    errors.clear();

    if (product == null) {
      errors.put("product", "product object is null");
      return errors;
    }
    if (!isProductPriceValid(product)) {
      errors.put("product price", "product price should be greater than zero");
    }

    if (!isProductNameValid(product)) {
      errors.put("product name", "product name should contain only capital letters and whitespaces");
    }


    return errors;
  }

  private boolean isProductNameValid(Product product) {
    return product.getName().matches("[A-Z]+[\\s]*[A-Z]*");
  }


  public boolean hasErrors() {
    return !errors.isEmpty();
  }

  private boolean isProductPriceValid(Product product) {
    return product.getPrice().compareTo(BigDecimal.ZERO) > 0;
  }


}
