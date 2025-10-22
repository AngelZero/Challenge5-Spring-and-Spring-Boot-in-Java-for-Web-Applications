package orders.service;

import orders.model.Order;
import orders.repo.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Application service encapsulating CRUD operations for {@link Order}.
 * <p>
 * This layer centralizes domain/application logic and keeps controllers thin.
 * </p>
 */
@Service
public class OrderService {
    private final OrderRepository repo;

    /**
     * Creates the service with its repository dependency.
     * @param repo JPA repository for orders
     */
    public OrderService(OrderRepository repo) {
        this.repo = repo;
    }

    /**
     * Persist a new order.
     * @param o order to be created
     * @return the saved entity, including generated {@code id}
     */
    public Order create(Order o) {
        return repo.save(o);
    }

    /**
     * Retrieve all orders.
     * @return list of orders (possibly empty)
     */
    public List<Order> findAll() {
        return repo.findAll();
    }

    /**
     * Retrieve a single order by id.
     * @param id primary key
     * @return the order if found, otherwise {@code null}
     */
    public Order findById(Long id) {
        return repo.findById(id).orElse(null);
    }

    /**
     * Update basic fields of an existing order.
     * @param id identifier of the order to update
     * @param input new field values
     * @return the updated order, or {@code null} if not found
     */
    public Order update(Long id, Order input) {
        return repo.findById(id).map(existing -> {
            existing.setNotes(input.getNotes());
            existing.setStatus(input.getStatus());
            existing.setTotal(input.getTotal());
            return repo.save(existing);
        }).orElse(null);
    }

    /**
     * Delete an order by id.
     * @param id primary key
     * @return {@code true} if the entity existed and was deleted; {@code false} otherwise
     */
    public boolean delete(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}
