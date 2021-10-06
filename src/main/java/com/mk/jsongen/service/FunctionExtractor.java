package com.mk.jsongen.service;

import com.mk.jsongen.model.pojo.DynamicList;
import com.mk.jsongen.model.pojo.Function;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
    private final Pattern patternArgsQuotesDelimiter = Pattern.compile("'(.+?)',?");

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

    private DynamicList extractArgs(String functionExpression) {
        Matcher argsMatcher = patternArgs.matcher(functionExpression);
        List<String> args = new ArrayList<>();
        if (argsMatcher.find()) {
            String argsMatch = argsMatcher.group();
            String plainArgs = argsMatch.substring(1, argsMatch.length() - 1);
            if (!StringUtils.isEmpty(plainArgs)) {
                if (plainArgs.contains("'")) {
                    args = extractProtectedArguments(plainArgs);
                } else {
                    args = extractUnprotectedArguments(plainArgs);
                }
            }
        }
        return DynamicList.of(args);
    }

    private List<String> extractUnprotectedArguments(String plainArgs) {
        return Arrays.stream(plainArgs.split(","))
                .map(StringUtils::trimWhitespace)
                .collect(Collectors.toList());
    }

    private List<String> extractProtectedArguments(String plainArgs) {
        return patternArgsQuotesDelimiter.matcher(plainArgs).results()
                .map(m -> m.group(1))
                .map(String::trim)
                .map(this::sanitizeProtectingQuotes)
                .collect(Collectors.toList());
    }

    private String sanitizeProtectingQuotes(String stringToSanitize) {
        String result = stringToSanitize;
        if (stringToSanitize.charAt(0) == '\'') {
            result = result.substring(1);
        }
        if (stringToSanitize.charAt(result.length() - 1) == '\'') {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }

    private String extractBody(String functionExpression) {
        Matcher bodyMatcher = patternBody.matcher(functionExpression);
        if (bodyMatcher.find()) {
            return bodyMatcher.group();
        }
        return "";
    }
}
