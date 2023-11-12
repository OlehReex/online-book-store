package onlinebookstore.mapper;

import onlinebookstore.dto.shopping.cart.ShoppingCartResponseDto;
import onlinebookstore.model.ShoppingCart;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        implementationPackage = "<PACKAGE_NAME>.impl",
        uses = CartItemMapper.class
)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    ShoppingCartResponseDto toResponseDto(ShoppingCart shoppingCart);
}
