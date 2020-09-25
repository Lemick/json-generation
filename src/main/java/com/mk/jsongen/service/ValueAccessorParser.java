package com.mk.jsongen.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.mk.jsongen.generator.contract.IGenerator;
import com.mk.jsongen.model.pojo.Function;
import com.mk.jsongen.model.pojo.accessor.DynamicValueAccessor;
import com.mk.jsongen.model.pojo.accessor.IValueAccessor;
import com.mk.jsongen.model.pojo.accessor.StaticValueAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValueAccessorParser {

    public static final Pattern pattern = Pattern.compile("\\{{2}(.*?}?)\\}{2}");

    @Autowired
    GeneratorFactory generatorFactory;

    @Autowired
    FunctionExtractor functionExtractor;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    SpelParserService spelParserService;


    public IValueAccessor generateContext(ValueNode valueNode) {
        if (!valueNode.isTextual()) {
            Object rawValue = extractNotTextualValue(valueNode);
            return StaticValueAccessor.builder().value(rawValue).build();
        }
        Matcher matcher = pattern.matcher(valueNode.asText());
        long countOccurences = matcher.results().count();
        if (countOccurences == 0) {
            return StaticValueAccessor.builder().value(valueNode.asText()).build();
        } else {
            return buildDynamicValueAccessor(valueNode);
        }
    }

    private DynamicValueAccessor buildDynamicValueAccessor(ValueNode valueNode) {
        String templateExpression = valueNode.asText();
        EvaluationContext evaluationContext = buildEvaluationContext(templateExpression);
        Expression parsedExpression = buildSpelExpression(templateExpression);
        return DynamicValueAccessor.builder()
                .evaluationContext(evaluationContext)
                .expression(parsedExpression)
                .build();
    }

    private EvaluationContext buildEvaluationContext(String templateExpression) {
        List<IGenerator> generators = new ArrayList<>();
        Matcher matcher = pattern.matcher(templateExpression);
        while (matcher.find()) {
            Function parsedFunction = functionExtractor.extract(matcher.group(1));
            generators.add(generatorFactory.create(parsedFunction));
        }
        return new StandardEvaluationContext(generators);
    }

    private Expression buildSpelExpression(String templateExpression) {
        Matcher matcher = pattern.matcher(templateExpression);
        AtomicInteger i = new AtomicInteger();
        String expression = matcher.replaceAll(m -> String.format("#{get(%d).generate()}", i.getAndIncrement()));
        return spelParserService.parseExpression(expression);
    }

    private Object extractNotTextualValue(ValueNode valueNode) {
        if (valueNode.isInt()) {
            return valueNode.asInt();
        } else if (valueNode.isLong()) {
            return valueNode.asLong();
        } else if (valueNode.isBoolean()) {
            return valueNode.asBoolean();
        } else {
            return valueNode.asText();
        }
    }

}
