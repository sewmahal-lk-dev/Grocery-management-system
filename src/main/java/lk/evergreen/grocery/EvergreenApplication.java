package lk.evergreen.grocery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;


@SpringBootApplication(scanBasePackages = "lk.evergreen.grocery")
@EntityScan("lk.evergreen.grocery.entity")
public class EvergreenApplication {
    public static void main(String[] args) {
        SpringApplication.run(EvergreenApplication.class, args);
    }
}
