package com.mk.jsongen.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class JsonTemplateGeneratorTest {

    @InjectMocks
    JsonTemplateGenerator model;

    @Spy
    TemplateConverter templateConverter;

    ObjectMapper om = new ObjectMapper();

    @Test
    public void recurseGenerateFromTemplate_empty() {
        ObjectNode template = JsonNodeFactory.instance.objectNode();
        ObjectNode actual = model.recurseGenerateFromTemplate(template);
        assertEquals(template, actual);
    }

    @Test
    public void recurseGenerateFromTemplate_different_types() {
        ObjectNode template = JsonNodeFactory.instance.objectNode()
                .put("valText", "myValText")
                .put("amIWonderful", true)
                .put("wonderfulLevel", 42);

        ObjectNode actual = model.recurseGenerateFromTemplate(template);
        assertEquals(template, actual);
    }

    @Test
    public void recurseGenerateFromTemplate_recursive() throws JsonProcessingException {
        ObjectNode innerObject = JsonNodeFactory.instance.objectNode()
                .put("myInnerField", "myInnerValue");
        ObjectNode template = JsonNodeFactory.instance.objectNode()
                .put("myField", "myValue")
                .set("myInnerObject", innerObject);


        ObjectNode actual = model.recurseGenerateFromTemplate(template);
        assertEquals(om.writeValueAsString(template), om.writeValueAsString(actual));
    }

}