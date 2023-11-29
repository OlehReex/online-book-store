package onlinebookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.order.CreateOrderRequestDto;
import onlinebookstore.dto.order.CreateOrderResponseDto;
import onlinebookstore.dto.order.OrderItemResponseDto;
import onlinebookstore.dto.order.OrderResponseDto;
import onlinebookstore.dto.order.OrderStatusResponseDto;
import onlinebookstore.dto.order.UpdateStatusRequestDto;
import onlinebookstore.repository.OrderRepository;
import onlinebookstore.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order manager", description = "Endpoints for order management")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get orders history",
            description = "Get list of all orders and information about everyone")
    public List<OrderResponseDto> getOrdersHistory() {
        return orderService.getHistory();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Create new order",
            description = "Take all cart items from shopping cart "
                          + "and create new order with those items")
    public CreateOrderResponseDto createOrder(
            @RequestBody @Valid CreateOrderRequestDto orderRequestDto) {
        return orderService.createOrder(orderRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get order items",
            description = "Get list of all items from order")
    public List<OrderItemResponseDto> getAllOrderItems(@PathVariable @Positive Long orderId) {
        return orderService.getAllItems(orderId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items/{id}")
    @Operation(summary = "Get item",
            description = "Get specific item from order")
    public OrderItemResponseDto getOrderItem(@PathVariable @Positive Long orderId,
                                             @PathVariable @Positive Long id) {
        return orderService.getSpecificItem(orderId, id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update status",
            description = "Change order status while order is in progress")
    public OrderStatusResponseDto updateStatus(
            @PathVariable @Positive Long id,
            @RequestBody @Valid UpdateStatusRequestDto statusRequest) {
        return orderService.updateStatus(id, statusRequest);
    }
}
