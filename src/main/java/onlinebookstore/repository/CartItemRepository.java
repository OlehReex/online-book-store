package onlinebookstore.repository;

import onlinebookstore.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByShoppingCartIdAndBookId(Long cartId, Long bookId);
}
