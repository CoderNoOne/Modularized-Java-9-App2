package converters.others;


import converters.json.OrderDTOJsonConverter;
import exceptions.AppException;
import model.Order;

import validator.OrderValidator;


import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class OrderServiceConverter {

  public List<Order> toOrderList(String jsonFile) {

    var orderValidator = new OrderValidator();
    AtomicInteger atomicInteger = new AtomicInteger(1);

    return new OrderDTOJsonConverter(jsonFile)
            .fromJson()
            .orElseThrow(() -> new AppException("FILE " + jsonFile + " is empty"))
            .stream()
            .map(OrderDTOtoOrderConverter::convert)
            .filter(order -> {

              Map<String, String> orderErrors = orderValidator.validate(order);

              if (orderValidator.hasErrors()) {

                System.out.println("ORDER NO: " + atomicInteger.get());
                System.out.println(orderErrors.entrySet().stream().map(e -> e.getKey() + " : " + e.getValue()).collect(Collectors.joining("\n")));
              }
              atomicInteger.incrementAndGet();
              return !orderValidator.hasErrors();
            }).collect(Collectors.toList());
  }
}

