package com.fastcapmus.board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class FastCampBoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastCampBoardApplication.class, args);
    }

}
