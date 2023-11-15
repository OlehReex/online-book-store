package onlinebookstore.mapper;

import onlinebookstore.dto.order.CreateOrderResponseDto;
import onlinebookstore.dto.order.OrderResponseDto;
import onlinebookstore.dto.order.OrderStatusResponseDto;
import onlinebookstore.model.Order;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
}
