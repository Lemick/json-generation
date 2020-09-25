package com.mk.jsongen.model.pojo.accessor;

import com.mk.jsongen.generator.IntGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DynamicValueAccessorTest {

    @Test
    public void accessValue() {
        SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
        TemplateParserContext templateParserContext = new TemplateParserContext();
        Expression expression = spelExpressionParser.parseExpression("#{get(0).generate()}", templateParserContext);
        EvaluationContext evaluationContext = new StandardEvaluationContext(
                List.of(IntGenerator.builder().min(1).max(1).build())
        );
        DynamicValueAccessor model = DynamicValueAccessor.builder()
                .evaluationContext(evaluationContext)
                .expression(expression).build();

        assertEquals(1, model.accessValue());
    }
}