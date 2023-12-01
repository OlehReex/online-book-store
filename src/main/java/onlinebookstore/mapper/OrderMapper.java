package onlinebookstore.mapper;

import java.math.BigDecimal;
import java.util.Set;
import onlinebookstore.dto.order.CreateOrderRequestDto;
import onlinebookstore.dto.order.CreateOrderResponseDto;
import onlinebookstore.dto.order.OrderResponseDto;
import onlinebookstore.dto.order.OrderStatusResponseDto;
import onlinebookstore.model.CartItem;
import onlinebookstore.model.Order;
import onlinebookstore.model.ShoppingCart;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl",
        uses = OrderItemMapper.class
)
public interface OrderMapper {
    CreateOrderResponseDto toCreateOrderResponseDto(Order order);

    @Mapping(source = "user.id", target = "userId")
    OrderResponseDto toResponseDto(Order order);

    OrderStatusResponseDto toStatusResponseDto(Order order);

    @Mappings({
            @Mapping(source = "shoppingCart.user", target = "user"),
            @Mapping(constant = "NEW", target = "status"),
            @Mapping(source = "orderRequestDto.shippingAddress", target = "shippingAddress"),
            @Mapping(source = "shoppingCart.cartItems", target = "total",
                    qualifiedByName = "totalPrice"),
            @Mapping(expression = "java(java.time.LocalDateTime.now())", target = "orderDate")})
    Order toOrderFromDtoAndCart(CreateOrderRequestDto orderRequestDto, ShoppingCart shoppingCart);

    @Named("totalPrice")
    default BigDecimal calculateTotalPrice(Set<CartItem> cartItems) {
        return cartItems.stream()
                        .map(ci -> ci.getBook().getPrice()
                                .multiply(new BigDecimal(ci.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
