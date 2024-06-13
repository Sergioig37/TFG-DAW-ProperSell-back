package es.proyecto.sergio;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProperSellApplication {
    private static final Logger logger
            = LoggerFactory.getLogger(ProperSellApplication.class);

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    public static void main(String[] args) {

    logger.error("ESTE ES MI LOG");
        SpringApplication.run(ProperSellApplication.class, args);



    }

}
