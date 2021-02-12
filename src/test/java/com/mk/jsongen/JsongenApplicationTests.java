package com.mk.jsongen;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        classes = JsongenApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class JsongenApplicationTests {

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @LocalServerPort
    private int port;

    private TestRestTemplate restTemplate;

    @PostConstruct
    public void setUp() {
        RestTemplateBuilder customRestBuilder = restTemplateBuilder.rootUri("http://localhost:" + port);
        restTemplate = new TestRestTemplate(customRestBuilder);
    }

    @Test
    public void _isAlive() {
        String result = restTemplate.exchange("/alive", HttpMethod.GET, null, String.class).getBody();
        assertEquals("I'm Alive", result);
    }

    @Test
    public void _generateFromTemplate_simple() {
        ObjectNode template = JsonNodeFactory.instance.objectNode()
                .put("id", "{{randInt}}")
                .put("firstName", "{{randFirstName()}}")
                .put("lastName", "{{randLastName()}}");

        ResponseEntity<List<JsonNode>> response = restTemplate.exchange(
                "/generate?size=5",
                HttpMethod.POST,
                new HttpEntity<>(template),
                new ParameterizedTypeReference<List<JsonNode>>() {
                });

        assertEquals(HttpStatus.OK, response.getStatusCode(), "reponse is OK");
        assertEquals(5, response.getBody().size(), "the number of docs returned is correct");

        for (JsonNode jsonNode : response.getBody()) {
            assertTrue(jsonNode.hasNonNull("id"), "the field has been generated");
            assertTrue(jsonNode.hasNonNull("firstName"), "the field has been generated");
            assertTrue(jsonNode.hasNonNull("lastName"), "the field has been generated");
        }
    }

}
