package com.mk.jsongen.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.mk.jsongen.generator.contract.IGenerator;
import com.mk.jsongen.model.pojo.Function;
import com.mk.jsongen.model.pojo.accessor.DynamicValueAccessor;
import com.mk.jsongen.model.pojo.accessor.IValueAccessor;
import com.mk.jsongen.model.pojo.accessor.StaticValueAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SpelParserService {

    @Autowired
    SpelExpressionParser spelExpressionParser;

    @Autowired
    TemplateParserContext templateParserContext;

    public Expression parseExpression(String spelExpression) {
        return spelExpressionParser.parseExpression(spelExpression, templateParserContext);
    }


}
