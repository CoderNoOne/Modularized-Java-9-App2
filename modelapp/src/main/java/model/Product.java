package model;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
  private String name;
  private Category category;
  private BigDecimal price;

  public Product(String name, Category category, BigDecimal price) {
    this.name = name;
    this.category = category;
    this.price = price;
  }

  public Product() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Product product = (Product) o;
    return Objects.equals(name, product.name) &&
            category == product.category &&
            Objects.equals(price, product.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, category, price);
  }

  @Override
  public String toString() {
    return "Product{" +
            "name='" + name + '\'' +
            ", category=" + category +
            ", price=" + price +
            '}';
  }
}
