package com.mk.jsongen.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.mk.jsongen.generator.contract.IGenerator;
import com.mk.jsongen.model.pojo.Function;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TemplateFieldCompilerFactory {

    public static final Pattern pattern = Pattern.compile("\\{{2}(.*?}?)\\}{2}");

    @Autowired
    GeneratorFactory generatorFactory;

    @Autowired
    FunctionExtractor functionExtractor;

    @Autowired
    ObjectMapper objectMapper;

    public ValueNode parseValue(ValueNode valueNode) {
        if (!valueNode.isTextual()) {
            return valueNode;
        }
        Matcher matcher = pattern.matcher(valueNode.asText());
        long countOccurences = matcher.results().count();
        if (countOccurences == 0) {
            return valueNode;
        } else if (countOccurences == 1 && cantItBeStrongTyped(valueNode.asText())) {
            matcher.reset();
            return parseUniqueValue(matcher);
        } else {
            matcher.reset();
            String parsedExpression = matcher.replaceAll(this::parseMultipleValues);
            return JsonNodeFactory.instance.textNode(parsedExpression);
        }
    }

    /**
     * Returns true if the string value can be casted to a more specific JSON type (number, boolean)
     * ex:
     * "    {{ randomBool() }}   " -> true
     * "my value {{ randomBool() }}" -> false
     */
    private boolean cantItBeStrongTyped(String textValue) {
        Matcher matcher = pattern.matcher(textValue);
        String textWithoutMatch = matcher.replaceFirst("");
        return StringUtils.isEmpty(textWithoutMatch);
    }

    private ValueNode parseUniqueValue(Matcher matcher) {
        matcher.find();
        Function parsedFunction = functionExtractor.extract(matcher.group(1));
        Object generatedValue = generatorFactory.create(parsedFunction).generate();
        return objectMapper.valueToTree(generatedValue);
    }

    /**
     * In case of multiple values, result will be a String
     */
    private String parseMultipleValues(MatchResult matchResult) {
        Function parsedFunction = functionExtractor.extract(matchResult.group(1));
        IGenerator generator = generatorFactory.create(parsedFunction);
        return String.valueOf(generator.generate());
    }
}
