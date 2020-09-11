package com.mk.jsongen.service;

import com.mk.jsongen.model.Function;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FunctionExtractor {

    String BEGIN_ARGS = "\\(";
    String END_ARGS = ")";

    public Function extract(String functionExpression) {
        functionExpression = StringUtils.trimAllWhitespace(functionExpression);
        String[] splitted = functionExpression.split(BEGIN_ARGS);
        String functionName = splitted[0];
        List<String> sanitizedArgs = List.of();
        if (splitted.length == 2) {
            String[] args = splitted[1].replace(END_ARGS, "").split(",");
            if (!StringUtils.isEmpty(args[0])) {
                sanitizedArgs = Arrays.stream(args)
                        .map(StringUtils::trimWhitespace)
                        .collect(Collectors.toList());
            }
        }
        return Function.builder().functionName(functionName).args(sanitizedArgs).build();
    }
}
