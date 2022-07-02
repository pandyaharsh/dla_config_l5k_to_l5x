package com.gm;

import com.gm.service.parserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class DlaConfigL5kToL5xApplication{

    public static void main(String[] args) throws IOException {
        SpringApplication.run(DlaConfigL5kToL5xApplication.class, args);

        parserService pr = new parserService();
        pr.read_file();
    }

}
