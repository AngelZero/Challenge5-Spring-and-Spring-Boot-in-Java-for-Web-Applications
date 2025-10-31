package orders.controller;

import orders.model.Order;
import orders.service.OrderService;
import jakarta.validation.Valid;
import orders.dto.OrderCreateRequest;
import orders.dto.OrderResponse;
import orders.dto.OrderUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * REST controller exposing CRUD endpoints for {@link Order} resources.
 * <p>
 * Base path: <code>/api/orders</code>
 * </p>
 * Typical lifecycle:
 * <ol>
 *   <li>Create an order via {@code POST /api/orders}</li>
 *   <li>List or retrieve via {@code GET /api/orders} or {@code GET /api/orders/{id}}</li>
 *   <li>Update via {@code PUT /api/orders/{id}}</li>
 *   <li>Delete via {@code DELETE /api/orders/{id}}</li>
 * </ol>
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    /**
     * Creates the controller with its required service dependency.
     * @param service application service handling order operations
     */
    public OrderController(OrderService service) {
        this.service = service;
    }

    /**
     * Create a new order.
     *
     * @param body validated request body representing the order to create
     * @return 201 Created with the persisted {@link Order} in the body and a
     *         {@code Location} header pointing to {@code /api/orders/{id}}
     */
    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderCreateRequest body) {
        OrderResponse saved = service.create(body);
        return ResponseEntity
                .created(URI.create("/api/orders/"))
                .body(saved);
    }

    /**
     * List all orders.
     *
     * @return 200 OK with a JSON array of {@link Order} items (possibly empty)
     */
    @GetMapping
    public List<OrderResponse> list() {
        return service.findAll();
    }

    /**
     * Retrieve an order by id.
     *
     * @param id database identifier of the order
     * @return 200 OK with the {@link Order} if found; 404 Not Found otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> get(@PathVariable Long id) {
        OrderResponse o = service.findById(id);
        return (o == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(o);
    }

    /**
     * Update an existing order.
     *
     * @param id   identifier of the order to update
     * @param body validated request body with new field values
     * @return 200 OK with the updated {@link Order} if found; 404 Not Found otherwise
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable Long id, @Valid @RequestBody OrderUpdateRequest body) {
        OrderResponse updated = service.update(id, body);
        return (updated == null) ? ResponseEntity.notFound().build() : ResponseEntity.ok(updated);
    }

    /**
     * Delete an order by id.
     *
     * @param id identifier of the order to delete
     * @return 204 No Content if deleted; 404 Not Found if the resource does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = service.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
