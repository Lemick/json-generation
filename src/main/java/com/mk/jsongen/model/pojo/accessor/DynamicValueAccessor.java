package com.mk.jsongen.model.pojo.accessor;

import com.mk.jsongen.generator.contract.IGenerator;
import lombok.Builder;
import lombok.Data;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.util.List;

@Data
public class DynamicValueAccessor implements IValueAccessor {

    private final String expression;
    private final List<IGenerator> generators;

    @Builder
    public DynamicValueAccessor(String expression, List<IGenerator> generators) {
        this.expression = expression;
        this.generators = generators;
    }

    @Override
    public Object accessValue() {
        String generatedExpression = expression;
        for(IGenerator generator : generators) {
            generatedExpression = generatedExpression.replaceFirst("#V#", generator.generate().toString());
        }
        return generatedExpression;
    }
}
