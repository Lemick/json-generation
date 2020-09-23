package com.mk.jsongen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;

@SpringBootApplication(exclude = ElasticsearchRestClientAutoConfiguration.class)
public class JsongenApplication {

	public static void main(String[] args) {
		SpringApplication.run(JsongenApplication.class, args);
	}

}
