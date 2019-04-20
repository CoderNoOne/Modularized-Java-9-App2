package converters.json;


import model.OrderDTO;

import java.util.List;

public class OrderDTOJsonConverter extends JsonConverter<List<OrderDTO>> {

  public OrderDTOJsonConverter(String jsonFilename) {
    super(jsonFilename);
  }
}