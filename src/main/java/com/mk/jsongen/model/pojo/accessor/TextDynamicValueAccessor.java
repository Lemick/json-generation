package com.mk.jsongen.model.pojo.accessor;

import com.mk.jsongen.generator.contract.IGenerator;
import com.mk.jsongen.service.ValueAccessorParser;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class TextDynamicValueAccessor implements IValueAccessor {

    private String[] tokens;
    private List<IGenerator> generators;

    @Builder
    public TextDynamicValueAccessor(String expression, List<IGenerator> generators) {
        this.tokens = expression.split(ValueAccessorParser.TEMPLATE_VAL_ID, -1);
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
