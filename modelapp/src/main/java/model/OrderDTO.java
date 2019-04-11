package model;


import java.util.Objects;

public class OrderDTO {

  private Customer customer;
  private Product product;
  private int quantity;
  private String orderDate;


  public OrderDTO() {
  }

  public OrderDTO(Customer customer, Product product, int quantity, String orderDate) {
    this.customer = customer;
    this.product = product;
    this.quantity = quantity;
    this.orderDate = orderDate;
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

  public String getOrderDate() {
    return orderDate;
  }

  public void setOrderDate(String orderDate) {
    this.orderDate = orderDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OrderDTO order = (OrderDTO) o;
    return quantity == order.quantity &&
            Objects.equals(customer, order.customer) &&
            Objects.equals(product, order.product) &&
            Objects.equals(orderDate, order.orderDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customer, product, quantity, orderDate);
  }

  @Override
  public String toString() {
    return "OrderDTO{" +
            "customer=" + customer +
            ", product=" + product +
            ", quantity=" + quantity +
            ", orderDate=" + orderDate +
            '}';
  }


}
