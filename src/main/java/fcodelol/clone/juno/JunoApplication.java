package fcodelol.clone.juno;

import fcodelol.clone.juno.interceptor.GatewayConstant;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableScheduling
public class JunoApplication {
    public static void main(String[] args) {
        GatewayConstant.addApiEntities();
        SpringApplication.run(JunoApplication.class, args);
    }

}
