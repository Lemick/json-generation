package com.mk.jsongen.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mk.jsongen.utils.SecureRandomUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.security.SecureRandom;
import java.util.stream.IntStream;

import static com.fasterxml.jackson.databind.node.JsonNodeFactory.instance;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TemplateConverterTest {

    @InjectMocks
    TemplateConverter model;

    @Spy
    GeneratorFactory factory;

    @Spy
    FunctionExtractor functionExtractor;

    @Spy
    ObjectMapper objectMapper;

    @Mock
    SecureRandom mockRandomizer;

    @Before
    public void before() {
        SecureRandomUtil.secureRandom = mockRandomizer;
    }

    @Test
    public void parseValue_no_template() {

        Object actual = model.parseValue(instance.textNode("hello world {}"));
        assertEquals(instance.textNode("hello world {}"), actual);
    }

    @Test
    public void parseValue_simple() {
        when(mockRandomizer.ints(anyInt(), anyInt())).thenAnswer(invocation -> IntStream.of(1));
        Object actual = model.parseValue(instance.textNode("{{randInt()}}"));
        assertEquals(instance.numberNode(1), actual);
    }

    @Test
    public void parseValue_withSpaces() {
        when(mockRandomizer.ints(anyInt(), anyInt())).thenAnswer(invocation -> IntStream.of(2));
        Object actual = model.parseValue(instance.textNode("{{  randInt(1)  }}"));
        assertEquals(instance.numberNode(2), actual);
    }

    @Test
    public void parseValue_many() {
        when(mockRandomizer.ints(anyInt(), anyInt())).thenAnswer(invocation -> IntStream.of(1));
        Object actual = model.parseValue(instance.textNode("le premier est {{randInt()}}, {{randBool()}}"));
        assertEquals(instance.textNode("le premier est 1, true"), actual);
    }

    @Test
    public void parseValue_boolean() {
        Object actual = model.parseValue(instance.booleanNode(true));
        assertEquals(instance.booleanNode(true), actual);
    }

}