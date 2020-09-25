package com.mk.jsongen.model.pojo.accessor;

import lombok.Builder;
import lombok.Data;
import org.elasticsearch.client.ml.dataframe.evaluation.Evaluation;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;

@Data
public class DynamicValueAccessor implements IValueAccessor {

    private final Expression expression;
    private final EvaluationContext evaluationContext;

    @Builder
    public DynamicValueAccessor(Expression expression, EvaluationContext evaluationContext) {
        this.expression = expression;
        this.evaluationContext = evaluationContext;
    }

    @Override
    public Object accessValue() {
        return expression.getValue(evaluationContext);
    }
}
