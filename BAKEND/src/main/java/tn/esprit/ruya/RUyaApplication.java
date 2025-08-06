package tn.esprit.ruya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication(scanBasePackages = "tn.esprit.ruya")
public class RUyaApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RUyaApplication.class, args);

    }

}
