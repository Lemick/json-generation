package com.mk.jsongen.model.pojo.accessor;

import com.mk.jsongen.generator.contract.IGenerator;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class DynamicValueAccessor implements IValueAccessor {

    public static final String TEMPLATE_VAL_ID = "#V#";
    public static final Pattern TEMPLATE_PATTERN = Pattern.compile(TEMPLATE_VAL_ID);

    private final String[] tokens;
    private final List<IGenerator> generators;

    @Builder
    public DynamicValueAccessor(String expression, List<IGenerator> generators) {
        this.tokens = expression.split(TEMPLATE_VAL_ID, -1);
        this.generators = generators;
    }

    @Override
    public Object accessValue() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < tokens.length; i++) {
            stringBuilder.append(tokens[i]);
            if (generators.size() > i) {
                stringBuilder.append(generators.get(i).generate().toString());
            }
        }
        return stringBuilder.toString();
    }
}
