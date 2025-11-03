package orders.service;

import orders.dto.OrderCreateRequest;
import orders.dto.OrderResponse;
import orders.dto.OrderUpdateRequest;
import orders.model.Order;
import orders.repo.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock OrderRepository repo;
    @InjectMocks OrderService service;

    @Test
    void create_ok() {
        OrderCreateRequest req = new OrderCreateRequest("n", "NEW", new BigDecimal("10.00"));
        Order saved = new Order();
        saved.setNotes("n"); saved.setStatus("NEW"); saved.setTotal(new BigDecimal("10.00"));
        // simula PK y createdAt
        // (si tu entidad los setea al persistir, puedes stubearlos aquí)
        when(repo.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            // simula "persist"
            try {
                var idField = Order.class.getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(o, 1L);
            } catch (Exception ignored) {}
            return o;
        });

        OrderResponse out = service.create(req);
        assertThat(out.status()).isEqualTo("NEW");
        verify(repo).save(any(Order.class));
    }

    @Test
    void update_notFound_returnsNull() {
        when(repo.findById(999L)).thenReturn(Optional.empty());
        var out = service.update(999L, new OrderUpdateRequest("x","PAID",new BigDecimal("1.00")));
        assertThat(out).isNull();
    }

    @Test
    void delete_whenExists_true() {
        when(repo.existsById(1L)).thenReturn(true);
        boolean ok = service.delete(1L);
        assertThat(ok).isTrue();
        verify(repo).deleteById(1L);
    }
}
