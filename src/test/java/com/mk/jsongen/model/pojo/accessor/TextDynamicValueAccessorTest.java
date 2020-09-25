package com.mk.jsongen.model.pojo.accessor;

import com.mk.jsongen.generator.IntGenerator;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static com.mk.jsongen.service.ValueAccessorParser.TEMPLATE_VAL_ID;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TextDynamicValueAccessorTest {

    @Test
    public void accessValue_one_token_one_template() {
        TextDynamicValueAccessor model = TextDynamicValueAccessor.builder()
                .expression("mon age est " + TEMPLATE_VAL_ID)
                .generators(List.of(
                        IntGenerator.builder().min(50).max(50).build()
                )).build();

        assertEquals("mon age est 50", model.accessValue());
    }

    @Test
    public void accessValue_two_tokens_one_template() {
        TextDynamicValueAccessor model = TextDynamicValueAccessor.builder()
                .expression("mon age est de " + TEMPLATE_VAL_ID + " ans")
                .generators(List.of(
                        IntGenerator.builder().min(50).max(50).build()
                )).build();

        assertEquals("mon age est de 50 ans", model.accessValue());
    }

    @Test
    public void accessValue_two_templates() {
        TextDynamicValueAccessor model = TextDynamicValueAccessor.builder()
                .expression(TEMPLATE_VAL_ID + TEMPLATE_VAL_ID)
                .generators(List.of(
                        IntGenerator.builder().min(1).max(1).build(),
                        IntGenerator.builder().min(2).max(2).build()
                )).build();

        assertEquals("12", model.accessValue());
    }

    @Test
    @Ignore // Need to find how to fix this
    public void accessValue_no_templates() {
        TextDynamicValueAccessor model = TextDynamicValueAccessor.builder()
                .expression("halo")
                .generators(List.of(
                        IntGenerator.builder().min(1).max(1).build(),
                        IntGenerator.builder().min(2).max(2).build()
                )).build();

        assertEquals("halo", model.accessValue());
    }

    @Test
    public void accessValue_two_tokens_three_templates() {
        TextDynamicValueAccessor model = TextDynamicValueAccessor.builder()
                .expression(TEMPLATE_VAL_ID + " or " + TEMPLATE_VAL_ID + " to " + TEMPLATE_VAL_ID)
                .generators(List.of(
                        IntGenerator.builder().min(25).max(25).build(),
                        IntGenerator.builder().min(6).max(6).build(),
                        IntGenerator.builder().min(4).max(4).build()
                )).build();

        assertEquals("25 or 6 to 4", model.accessValue());
    }
}