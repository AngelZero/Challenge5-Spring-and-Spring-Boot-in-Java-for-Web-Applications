package orders.repo;

import orders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link Order} entities.
 * <p>
 * Inherits CRUD and pagination methods from {@link JpaRepository}.
 * </p>
 */
public interface OrderRepository extends JpaRepository<Order, Long> {}
