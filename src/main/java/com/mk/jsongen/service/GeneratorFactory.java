package com.mk.jsongen.service;

import com.mk.jsongen.generator.*;
import com.mk.jsongen.generator.contract.IGenerator;
import com.mk.jsongen.model.Function;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.Instant.*;

@Service
public class GeneratorFactory {

    public static final String FUNCTION_LAST_NAME = "lastname";
    public static final String FUNCTION_FIRST_NAME = "firstname";
    public static final String FUNCTION_EMAIL = "email";
    public static final String FUNCTION_INT = "randint";
    public static final String FUNCTION_BOOL = "randbool";
    public static final String FUNCTION_DATE = "randdate";

    final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

    public IGenerator create(Function function) {
        List<String> args = function.getArgs();
        switch (function.getFunctionName().toLowerCase()) {
            case FUNCTION_FIRST_NAME:
                return FirstNameGenerator.builder().build();
            case FUNCTION_LAST_NAME:
                return LastNameGenerator.builder().build();
            case FUNCTION_EMAIL:
                return EmailGenerator.builder().build();
            case FUNCTION_INT:
                int min = args.size() >= 1 ? Integer.parseInt(args.get(0)) : 0;
                int max = args.size() >= 2 ? Integer.parseInt(args.get(1)) : Integer.MAX_VALUE - 1;
                return IntGenerator.builder().min(min).max(max).build();
            case FUNCTION_BOOL:
                return BoolGenerator.builder().build();
            case FUNCTION_DATE:
                Instant minDate = args.size() >= 1 ? from(formatter.parse(args.get(0))) : MIN;
                Instant maxDate = args.size() >= 2 ? from(formatter.parse(args.get(1))) : now();
                return DateGenerator.builder().min(minDate).max(maxDate).build();
            default:
                throw new IllegalArgumentException("No generated found for function name " + function.getFunctionName());
        }
    }
}
