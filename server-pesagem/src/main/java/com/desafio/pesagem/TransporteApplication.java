package com.desafio.pesagem;

import com.desafio.pesagem.config.TestContainersConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(TestContainersConfig.class)
public class TransporteApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransporteApplication.class, args);
    }
}
