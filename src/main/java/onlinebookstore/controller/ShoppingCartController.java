package onlinebookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    @Operation(summary = "Get shopping cart",
            description = "Get shopping cart information and list of all cart items")
    public ShoppingCartResponseDto getShoppingCart() {
        return shoppingCartService.getShoppingCart();
    }

    @PostMapping()
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add book", description = "Add new item to shopping cart")
    public CartItemResponseDto addBookToCart(
            @RequestBody @Valid CartItemRequestDto cartItemRequestDto) {
        return shoppingCartService.addItemToCart(cartItemRequestDto);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update quantity", description = "Update item quantity")
    public CartItemQuantityDto updateBookQuantity(
            @PathVariable @Positive Long cartItemId,
            @RequestBody @Valid CartItemQuantityDto quantityDto) {
        return shoppingCartService.changeQuantity(cartItemId, quantityDto);
    }

    @DeleteMapping("/books/{id}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Remove book", description = "Remove item from shopping cart")
    public ShoppingCartResponseDto removeBookFromCart(@PathVariable @Positive Long id) {
        return shoppingCartService.removeCartItem(id);
    }
}
