package com.mk.jsongen.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mk.jsongen.model.pojo.Function;
import com.mk.jsongen.model.pojo.accessor.DynamicValueAccessor;
import com.mk.jsongen.model.pojo.accessor.IValueAccessor;
import com.mk.jsongen.model.pojo.accessor.StaticValueAccessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.Expression;

import java.util.stream.IntStream;

import static com.fasterxml.jackson.databind.node.JsonNodeFactory.instance;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ValueAccessorParserTest {

    @InjectMocks
    ValueAccessorParser model;

    @Mock
    GeneratorFactory generatorFactory;

    @Mock
    FunctionExtractor functionExtractor;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    SpelParserService spelParserService;

    @Test
    public void generateContext_no_template() {
        IValueAccessor actual = model.generateContext(instance.textNode("hello world {}"));
        StaticValueAccessor expected = StaticValueAccessor.builder().value("hello world {}").build();
        assertEquals(expected, actual);
    }

    @Test
    public void generateContext_no_template_int() {
        IValueAccessor actual = model.generateContext(instance.numberNode(1));
        StaticValueAccessor expected = StaticValueAccessor.builder().value(1).build();
        assertEquals(expected, actual);
    }

    @Test
    public void generateContext_no_template_bool() {
        IValueAccessor actual = model.generateContext(instance.booleanNode(true));
        StaticValueAccessor expected = StaticValueAccessor.builder().value(true).build();
        assertEquals(expected, actual);
    }

    @Test
    public void generateContext_dynamic() {
        Function mockedFunction = Function.builder().functionName("randInt").build();
        when(functionExtractor.extract(any())).thenReturn(mockedFunction);
        when(generatorFactory.create(mockedFunction)).thenReturn(() -> 1);
        Expression expression = mock(Expression.class);
        when(expression.getExpressionString()).thenReturn("#{generator[0].generate()}");
        when(spelParserService.parseExpression(any())).thenReturn(expression);
        IValueAccessor actual = model.generateContext(instance.textNode("{{  randInt()  }}"));

        assertTrue(actual instanceof DynamicValueAccessor);
        String actualExpression = ((DynamicValueAccessor) actual).getExpression().getExpressionString();
        assertEquals("#{generator[0].generate()}", actualExpression);
    }

    @Test
    public void generateContext_preserve_curly_brackets() {
        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        model.generatorFactory = mock(GeneratorFactory.class);
        when(model.generatorFactory.create(any())).thenReturn(() -> "");

        model.generateContext(instance.textNode("{{function() {}}}"));
        verify(functionExtractor).extract(argument.capture());
        assertEquals("curly brackets of function must be preserved", "function() {}", argument.getValue());
    }


}