package com.mk.jsongen.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.mk.jsongen.generator.contract.IGenerator;
import com.mk.jsongen.model.pojo.Function;
import com.mk.jsongen.model.pojo.accessor.DynamicValueAccessor;
import com.mk.jsongen.model.pojo.accessor.TextDynamicValueAccessor;
import com.mk.jsongen.model.pojo.accessor.IValueAccessor;
import com.mk.jsongen.model.pojo.accessor.StaticValueAccessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TemplateParser {

    public static final Pattern pattern = Pattern.compile("\\{{2}(.*?}?)\\}{2}");
    public static final String TEMPLATE_VAL_ID = "#V#";

    @Autowired
    GeneratorFactory generatorFactory;

    @Autowired
    FunctionExtractor functionExtractor;

    public IValueAccessor generateContext(ValueNode valueNode) {
        if (!valueNode.isTextual()) {
            Object rawValue = extractNotTextualValue(valueNode);
            return StaticValueAccessor.builder().value(rawValue).build();
        }
        Matcher matcher = pattern.matcher(valueNode.asText());
        long countOccurences = matcher.results().count();
        if (countOccurences == 0) {
            return StaticValueAccessor.builder().value(valueNode.asText()).build();
        } else if (countOccurences == 1 && canTemplateBeTyped(valueNode.asText())) {
            List<IGenerator> generators = buildGenerators(valueNode.asText());
            return DynamicValueAccessor.builder()
                    .generator(generators.get(0))
                    .build();
        } else
            return buildTextDynamicValueAccessor(valueNode);
    }


    private TextDynamicValueAccessor buildTextDynamicValueAccessor(ValueNode valueNode) {
        String templateExpression = valueNode.asText();
        List<IGenerator> generators = buildGenerators(templateExpression);
        String dynamicExpression = buildDynamicExpression(templateExpression);
        return TextDynamicValueAccessor.builder()
                .generators(generators)
                .expression(dynamicExpression)
                .build();
    }

    private boolean canTemplateBeTyped(String textValue) {
        Matcher matcher = pattern.matcher(textValue);
        String textWithoutMatch = matcher.replaceFirst("");
        return StringUtils.isBlank(textWithoutMatch);
    }

    private List<IGenerator> buildGenerators(String templateExpression) {
        List<IGenerator> generators = new ArrayList<>();
        Matcher matcher = pattern.matcher(templateExpression);
        while (matcher.find()) {
            Function parsedFunction = functionExtractor.extract(matcher.group(1));
            generators.add(generatorFactory.create(parsedFunction));
        }
        return generators;
    }

    private String buildDynamicExpression(String templateExpression) {
        Matcher matcher = pattern.matcher(templateExpression);
        return matcher.replaceAll(TEMPLATE_VAL_ID);
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
