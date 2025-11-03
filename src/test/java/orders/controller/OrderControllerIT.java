package orders.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import orders.dto.OrderCreateRequest;
import orders.dto.OrderUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderControllerIT {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @Test
    void create_then_get_list_update_delete_happyPath() throws Exception {
        // create
        var req = new OrderCreateRequest("notes","NEW",new BigDecimal("12.34"));
        var createRes = mvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("NEW"))
                .andReturn();

        // extract id
        var json = createRes.getResponse().getContentAsString();
        long id = om.readTree(json).get("id").asLong();

        // get
        mvc.perform(get("/api/orders/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        // list
        mvc.perform(get("/api/orders"))
                .andExpect(status().isOk());

        // update
        var upd = new OrderUpdateRequest("updated","PAID",new BigDecimal("20.00"));
        mvc.perform(put("/api/orders/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(upd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));

        // delete
        mvc.perform(delete("/api/orders/{id}", id))
                .andExpect(status().isNoContent());

        // get 404
        mvc.perform(get("/api/orders/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void validation_errors_400() throws Exception {
        var bad = new OrderCreateRequest(null,"",new BigDecimal("-1.00"));
        mvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(bad)))
                .andExpect(status().isBadRequest());
    }
}
