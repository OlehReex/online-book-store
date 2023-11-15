package onlinebookstore.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.order.CreateOrderRequestDto;
import onlinebookstore.dto.order.CreateOrderResponseDto;
import onlinebookstore.dto.order.OrderItemResponseDto;
import onlinebookstore.dto.order.OrderResponseDto;
import onlinebookstore.dto.order.OrderStatusResponseDto;
import onlinebookstore.dto.order.UpdateStatusRequestDto;
import onlinebookstore.exception.EntityNotFoundException;
import onlinebookstore.mapper.OrderItemMapper;
import onlinebookstore.mapper.OrderMapper;
import onlinebookstore.model.CartItem;
import onlinebookstore.model.Order;
import onlinebookstore.model.OrderItem;
import onlinebookstore.model.ShoppingCart;
import onlinebookstore.model.Status;
import onlinebookstore.model.User;
import onlinebookstore.repository.OrderItemRepository;
import onlinebookstore.repository.OrderRepository;
import onlinebookstore.repository.ShoppingCartRepository;
import onlinebookstore.repository.UserRepository;
import onlinebookstore.service.OrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public CreateOrderResponseDto createOrder(CreateOrderRequestDto orderRequestDto) {
        Order order = initializeOrder(orderRequestDto,
                shoppingCartRepository.findByUserId(getUser().getId()).orElseThrow(() ->
                        new EntityNotFoundException("Can't find user")));
        return orderMapper.toCreateOrderResponseDto(orderRepository.save(order));
    }

    @Override
    public List<OrderResponseDto> getHistory() {
        List<Order> orderList = orderRepository.findAllByUserId(getUser().getId());
        return orderList.stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Override
    public OrderStatusResponseDto updateStatus(Long id, UpdateStatusRequestDto statusRequestDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order: " + id));
        order.setStatus(Status.valueOf(statusRequestDto.status()));
        return orderMapper.toStatusResponseDto(orderRepository.save(order));
    }

    @Transactional
    @Override
    public List<OrderItemResponseDto> getAllItems(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order: " + orderId));
        return order.getOrderItems().stream()
                .map(orderItemMapper::toResponseDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto getSpecificItem(Long orderId, Long id) {
        OrderItem orderItem = orderItemRepository.findByOrderIdAndId(orderId, id)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Can't find item with id %s in order", id)));
        return orderItemMapper.toResponseDto(orderItem);
    }

    private Order initializeOrder(CreateOrderRequestDto orderRequestDto,
                                  ShoppingCart shoppingCart) {
        Order newOrder = new Order();
        newOrder.setUser(shoppingCart.getUser());
        newOrder.setStatus(Status.NEW);
        newOrder.setShippingAddress(orderRequestDto.shippingAddress());
        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setTotal(getTotal(shoppingCart));
        orderRepository.save(newOrder);
        newOrder.setOrderItems(createOrderItems(newOrder, shoppingCart));
        cleanShoppingCart(shoppingCart);
        return newOrder;
    }

    private Set<OrderItem> createOrderItems(Order order, ShoppingCart shoppingCart) {
        return shoppingCart.getCartItems().stream()
                .map(i -> createNewOrderItem(order, i))
                .collect(Collectors.toSet());
    }

    private OrderItem createNewOrderItem(Order order, CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getBook().getPrice());
        return orderItemRepository.save(orderItem);
    }

    private BigDecimal getTotal(ShoppingCart shoppingCart) {
        return new BigDecimal(
                String.valueOf(shoppingCart.getCartItems().stream()
                .map(c -> c.getBook().getPrice()
                        .multiply(new BigDecimal(c.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)));
    }

    private void cleanShoppingCart(ShoppingCart shoppingCart) {
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
    }

    private User getUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(user.getEmail()).orElseThrow(() ->
                new EntityNotFoundException("Can't find user with username: " + user.getEmail()));
    }
}
