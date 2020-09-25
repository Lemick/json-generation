package com.mk.jsongen.service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.fasterxml.jackson.databind.util.RawValue;
import com.mk.jsongen.model.pojo.CompiledNodeContainer;
import com.mk.jsongen.model.pojo.CompiledNodeField;
import com.mk.jsongen.model.pojo.ICompiledNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JsonTemplateGeneratorTest {

    @InjectMocks
    JsonTemplateGenerator model;

    @Mock
    TemplateParser templateParser;

    @Mock
    ValueAccessorParser valueAccessorParser;

    ObjectMapper om = new ObjectMapper();
    

    @Test
    public void compileTemplate() {
        ObjectNode template = JsonNodeFactory.instance.objectNode()
                .put("myField", "myValue")
                .set("myObject", JsonNodeFactory.instance.objectNode().put("myInnerField", "myInnerValue"));

        when(valueAccessorParser.generateContext(any())).thenReturn(() -> "myValue");
        CompiledNodeContainer actual = model.compileTemplate(template);
        assertTrue(actual.getChilds().containsKey("myField"));
        assertTrue(actual.getChilds().containsKey("myObject"));

        ICompiledNode actualInnerNode = actual.getChilds().get("myObject");
        assertTrue(actualInnerNode instanceof CompiledNodeContainer);
        assertTrue(((CompiledNodeContainer) actualInnerNode).getChilds().containsKey("myInnerField"));
    }
}