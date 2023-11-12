package onlinebookstore.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.shopping.cart.CartItemQuantityDto;
import onlinebookstore.dto.shopping.cart.CartItemRequestDto;
import onlinebookstore.dto.shopping.cart.CartItemResponseDto;
import onlinebookstore.dto.shopping.cart.ShoppingCartResponseDto;
import onlinebookstore.service.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart manager",
        description = "Endpoints for shopping cart management")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping()
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartResponseDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    public CartItemResponseDto addBookToCart(@RequestBody CartItemRequestDto cartItemRequestDto) {
        return shoppingCartService.addItemToCart(cartItemRequestDto);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    public CartItemQuantityDto updateBookQuantity(
            @PathVariable @Positive Long cartItemId, @RequestBody CartItemQuantityDto quantityDto) {
        return shoppingCartService.changeQuantity(cartItemId, quantityDto);
    }

    @DeleteMapping("/books/{id}")
    @PreAuthorize("hasRole('USER')")
    public ShoppingCartResponseDto removeBookFromCart(@PathVariable @Positive Long id) {
        return shoppingCartService.removeCartItem(id);
    }
}
