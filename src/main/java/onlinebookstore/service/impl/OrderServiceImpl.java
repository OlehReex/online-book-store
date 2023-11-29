package onlinebookstore.service.impl;

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

    @Transactional
    @Override
    public CreateOrderResponseDto createOrder(CreateOrderRequestDto orderRequestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(
                getUser().getId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find user"));
        Order newOrder = orderMapper.toOrderFromDtoAndCart(orderRequestDto, shoppingCart);
        newOrder.setOrderItems(createOrderItems(newOrder, shoppingCart));
        cleanShoppingCart(shoppingCart);
        return orderMapper.toCreateOrderResponseDto(orderRepository.save(newOrder));
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

    private Set<OrderItem> createOrderItems(Order order, ShoppingCart shoppingCart) {
        return shoppingCart.getCartItems().stream()
                .map(i -> createNewOrderItem(order, i))
                .collect(Collectors.toSet());
    }

    private OrderItem createNewOrderItem(Order order, CartItem cartItem) {
        OrderItem orderItem = orderItemMapper.toOrder(order, cartItem);
        return orderItemRepository.save(orderItem);
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
