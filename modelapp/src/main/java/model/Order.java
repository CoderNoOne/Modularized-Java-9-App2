package model;

import java.time.LocalDate;
import java.util.Objects;

public class Order {

  private Customer customer;
  private Product product;
  private int quantity;
  private LocalDate orderDate;

  public Order() {
  }

  public Order(Customer customer, Product product, int quantity, LocalDate orderDate) {
    this.customer = customer;
    this.product = product;
    this.quantity = quantity;
    this.orderDate = orderDate;
  }

  private Order(OrderBuilder orderBuilder) {
    this.customer = orderBuilder.customer;
    this.product = orderBuilder.product;
    this.quantity = orderBuilder.quantity;
    this.orderDate = orderBuilder.orderDate;
  }


  @Override
  public boolean equals(Object o) {

    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Order that = (Order) o;
    return quantity == that.quantity &&
            Objects.equals(customer, that.customer) &&
            Objects.equals(product, that.product) &&
            Objects.equals(orderDate, that.orderDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customer, product, quantity, orderDate);
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public LocalDate getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(LocalDate orderDate) {
    this.orderDate = orderDate;
  }

  @Override
  public String toString() {
    return "Order{" +
            "customer=" + customer +
            ", product=" + product +
            ", quantity=" + quantity +
            ", orderDate=" + orderDate +
            '}';
  }

  public static class OrderBuilder{
    private Customer customer;
    private Product product;
    private int quantity;
    private LocalDate orderDate;

    public OrderBuilder customer(Customer customer){
      this.customer = customer;
      return this;
    }

    public OrderBuilder product (Product product) {
      this.product = product;
      return this;
    }
    public OrderBuilder quantity (int quantity){
      this.quantity = quantity;
      return this;
    }

    public OrderBuilder orderDate(LocalDate orderDate) {
      this.orderDate = orderDate;
      return this;
    }

    public Order build(){
      return new Order(this);
    }

  }

}
