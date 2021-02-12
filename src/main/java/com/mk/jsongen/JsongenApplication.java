package com.mk.jsongen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = ElasticsearchRestClientAutoConfiguration.class)
@RestController
public class JsongenApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsongenApplication.class, args);
    }

    @GetMapping("/alive")
    public String isAlive() {
        return "I'm Alive";
    }

}
