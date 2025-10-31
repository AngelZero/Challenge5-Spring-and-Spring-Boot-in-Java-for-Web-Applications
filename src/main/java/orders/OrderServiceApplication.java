package orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/** Spring Boot entry point for the Order Service application. */
@SpringBootApplication
public class OrderServiceApplication {
    /**
     * Bootstraps the Spring application.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
