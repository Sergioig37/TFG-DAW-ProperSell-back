package es.proyecto.sergio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProperSellApplication {
    private static final Logger logger
            = LoggerFactory.getLogger(ProperSellApplication.class);

    public static void main(String[] args) {

    logger.error("ESTE ES MI LOG");
        SpringApplication.run(ProperSellApplication.class, args);



    }

}
