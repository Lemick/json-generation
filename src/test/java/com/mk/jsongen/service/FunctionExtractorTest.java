package com.mk.jsongen.service;

import com.mk.jsongen.model.Function;
import com.mk.jsongen.service.FunctionExtractor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
public class FunctionExtractorTest {

    @InjectMocks
    FunctionExtractor model;

    @Test
    public void _extract_with_whitespaces() {
        Function actual = model.extract(" randInt(4 ,5) ");
        Function expected = Function.builder().functionName("randInt").args(List.of("4", "5")).build();
        assertEquals(expected, actual);
    }

    @Test
    public void _extract_no_args() {
        Function actual = model.extract("randInt()");
        Function expected = Function.builder().functionName("randInt").args(List.of()).build();
        assertEquals(expected, actual);
    }

    @Test
    public void _extract_no_parenthesis() {
        Function actual = model.extract("randInt");
        Function expected = Function.builder().functionName("randInt").args(List.of()).build();
        assertEquals(expected, actual);
    }

    @Test
    public void _extract_one_arg() {
        Function actual = model.extract("randInt(4)");
        Function expected = Function.builder().functionName("randInt").args(List.of("4")).build();
        assertEquals(expected, actual);
    }
}