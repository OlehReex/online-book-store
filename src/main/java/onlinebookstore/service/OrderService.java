package onlinebookstore.service;

import java.util.List;
import onlinebookstore.dto.order.CreateOrderRequestDto;
import onlinebookstore.dto.order.CreateOrderResponseDto;
import onlinebookstore.dto.order.OrderItemResponseDto;
import onlinebookstore.dto.order.OrderResponseDto;
import onlinebookstore.dto.order.OrderStatusResponseDto;
import onlinebookstore.dto.order.UpdateStatusRequestDto;

public interface OrderService {
    CreateOrderResponseDto createOrder(CreateOrderRequestDto orderRequestDto);

    List<OrderResponseDto> getHistory();

    OrderStatusResponseDto updateStatus(Long id, UpdateStatusRequestDto statusRequestDto);

    List<OrderItemResponseDto> getAllItems(Long orderId);

    OrderItemResponseDto getSpecificItem(Long orderId, Long id);
}
