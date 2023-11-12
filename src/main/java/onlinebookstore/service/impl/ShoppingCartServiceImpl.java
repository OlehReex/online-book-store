package onlinebookstore.service.impl;

import lombok.RequiredArgsConstructor;
import onlinebookstore.dto.shopping.cart.CartItemQuantityDto;
import onlinebookstore.dto.shopping.cart.CartItemRequestDto;
import onlinebookstore.dto.shopping.cart.CartItemResponseDto;
import onlinebookstore.dto.shopping.cart.ShoppingCartResponseDto;
import onlinebookstore.exception.EntityNotFoundException;
import onlinebookstore.mapper.CartItemMapper;
import onlinebookstore.mapper.ShoppingCartMapper;
import onlinebookstore.model.Book;
import onlinebookstore.model.CartItem;
import onlinebookstore.model.ShoppingCart;
import onlinebookstore.model.User;
import onlinebookstore.repository.BookRepository;
import onlinebookstore.repository.CartItemRepository;
import onlinebookstore.repository.ShoppingCartRepository;
import onlinebookstore.repository.UserRepository;
import onlinebookstore.service.ShoppingCartService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartResponseDto getShoppingCart() {
        return shoppingCartMapper.toResponseDto(getUserCart());
    }

    @Override
    public CartItemResponseDto addItemToCart(CartItemRequestDto cartItemRequestDto) {
        Book book = bookRepository.findById(cartItemRequestDto.bookId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find book with id: "
                        + cartItemRequestDto.bookId()));
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCartRepository.findByUserId(
                getUser().getId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find shopping cart")));
        cartItem.setQuantity(cartItemRequestDto.quantity());
        return cartItemMapper.toResponseDto(cartItemRepository.save(cartItem));
    }

    @Override
    public ShoppingCartResponseDto removeCartItem(Long bookId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart does not exists"));
        CartItem itemToDelete = cartItemRepository
                .findByShoppingCartIdAndBookId(shoppingCart.getId(), bookId);
        cartItemRepository.deleteById(itemToDelete.getId());
        return shoppingCartMapper.toResponseDto(getUserCart());
    }

    @Override
    public CartItemQuantityDto changeQuantity(
            Long cartItemId, CartItemQuantityDto quantityDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cart item with id: " + cartItemId));
        cartItem.setQuantity(quantityDto.quantity());
        cartItemRepository.save(cartItem);
        return quantityDto;
    }

    private ShoppingCart getUserCart() {
        User user = getUser();
        return shoppingCartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    ShoppingCart newShoppingCart = new ShoppingCart();
                    newShoppingCart.setUser(user);
                    return shoppingCartRepository.save(newShoppingCart);
                });
    }

    private User getUser() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(userName).orElseThrow(() ->
                new EntityNotFoundException("Can't find user with username: " + userName));
    }
}
