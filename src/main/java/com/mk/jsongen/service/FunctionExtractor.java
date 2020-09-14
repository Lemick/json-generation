package com.mk.jsongen.service;

import com.mk.jsongen.model.Function;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FunctionExtractor {

    private final Pattern patternFunctionName = Pattern.compile("(\\w+)");
    private final Pattern patternArgs = Pattern.compile("(\\(.*?\\))");
    private final Pattern patternBody = Pattern.compile("(\\{.*})");

    public Function extract(String functionExpression) {
        functionExpression = StringUtils.trimWhitespace(functionExpression);
        return Function.builder()
                .functionName(extractFunctionName(functionExpression))
                .args(extractArgs(functionExpression))
                .body(extractBody(functionExpression))
                .build();
    }

    private String extractFunctionName(String functionExpression) {
        Matcher functionNameMatcher = patternFunctionName.matcher(functionExpression);
        if (functionNameMatcher.find()) {
            return functionNameMatcher.group();
        }
        return "";
    }

    private List<String> extractArgs(String functionExpression) {
        Matcher argsMatcher = patternArgs.matcher(functionExpression);
        if (argsMatcher.find()) {
            String argsMatch = argsMatcher.group();
            String plainArgs = argsMatch.substring(1, argsMatch.length() - 1);
            if (!StringUtils.isEmpty(plainArgs)) {
                return Arrays.stream(plainArgs.split(","))
                        .map(StringUtils::trimWhitespace)
                        .collect(Collectors.toList());
            }
        }
        return List.of();
    }

    private String extractBody(String functionExpression) {
        Matcher bodyMatcher = patternBody.matcher(functionExpression);
        if (bodyMatcher.find()) {
            return bodyMatcher.group();
        }
        return "";
    }
}
