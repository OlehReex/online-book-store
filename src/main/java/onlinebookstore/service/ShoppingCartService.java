package onlinebookstore.service;

import onlinebookstore.dto.shopping.cart.CartItemQuantityDto;
import onlinebookstore.dto.shopping.cart.CartItemRequestDto;
import onlinebookstore.dto.shopping.cart.CartItemResponseDto;
import onlinebookstore.dto.shopping.cart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCart();

    CartItemResponseDto addItemToCart(CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto removeCartItem(Long bookId);

    CartItemQuantityDto changeQuantity(Long cartItemId, CartItemQuantityDto quantityDto);
}
