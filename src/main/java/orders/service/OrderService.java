package orders.service;

import orders.dto.OrderCreateRequest;
import orders.dto.OrderResponse;
import orders.dto.OrderUpdateRequest;
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
     * @param req order to be created
     * @return the saved entity, including generated {@code id}
     */
    public OrderResponse create(OrderCreateRequest req) {
        Order o = new Order();
        o.setNotes(req.notes());
        o.setStatus(req.status());
        o.setTotal(req.total());
        Order saved = repo.save(o);
        return toResponse(saved);
    }

    /**
     * Retrieve all orders.
     * @return list of orders (possibly empty)
     */
    public List<OrderResponse> findAll() {
        return repo.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Retrieve a single order by id.
     * @param id primary key
     * @return the order if found, otherwise {@code null}
     */
    public OrderResponse findById(Long id) {
        return repo.findById(id)
                .map(this::toResponse)
                .orElse(null);
    }

    /**
     * Update basic fields of an existing order.
     * @param id identifier of the order to update
     * @param req new field values
     * @return the updated order, or {@code null} if not found
     */
    public OrderResponse update(Long id, OrderUpdateRequest req) {
        return repo.findById(id).map(existing -> {
            existing.setNotes(req.notes());
            existing.setStatus(req.status());
            existing.setTotal(req.total());
            Order saved = repo.save(existing);
            return toResponse(saved);
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

    private OrderResponse toResponse(Order o) {
        return new OrderResponse(
                //o.getId(),
                o.getNotes(),
                o.getStatus(),
                o.getTotal()
                //o.getCreatedAt()
        );
    }
}
