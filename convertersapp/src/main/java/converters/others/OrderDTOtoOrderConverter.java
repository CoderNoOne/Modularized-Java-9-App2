package converters.others;

import model.OrderDTO;
import model.Order;

import java.time.LocalDate;

public class OrderDTOtoOrderConverter {

  private OrderDTOtoOrderConverter() {
  }

  public static Order convert(OrderDTO orderDTO) {

    return new Order.OrderBuilder()
            .customer(orderDTO.getCustomer())
            .product(orderDTO.getProduct())
            .quantity(orderDTO.getQuantity())
            .orderDate(LocalDate.parse(orderDTO.getOrderDate()))
            .build();
  }
}
