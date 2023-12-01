package onlinebookstore.mapper;

import onlinebookstore.dto.order.OrderItemResponseDto;
import onlinebookstore.model.CartItem;
import onlinebookstore.model.Order;
import onlinebookstore.model.OrderItem;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl"
)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toResponseDto(OrderItem orderItem);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "order", target = "order"),
            @Mapping(source = "cartItem.book", target = "book"),
            @Mapping(source = "cartItem.quantity", target = "quantity"),
            @Mapping(source = "cartItem.book.price", target = "price")})
    OrderItem toOrder(Order order, CartItem cartItem);
}
