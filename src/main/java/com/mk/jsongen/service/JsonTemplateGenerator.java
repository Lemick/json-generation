package com.mk.jsongen.service;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.mk.jsongen.model.pojo.CompiledNodeField;
import com.mk.jsongen.model.pojo.CompiledNodeContainer;
import com.mk.jsongen.model.pojo.accessor.IValueAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JsonTemplateGenerator {

    @Autowired
    TemplateParser templateParser;

    @Autowired
    ValueAccessorParser valueAccessorParser;

    private final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    public ObjectNode recurseGenerateFromTemplate(ObjectNode templateNode) {
        ObjectNode nodeParsed = nodeFactory.objectNode();
        templateNode.fields().forEachRemaining(entry -> {
            if (entry.getValue().isValueNode()) {
                ValueNode parsedValueNode = templateParser.parseValue((ValueNode) entry.getValue());
                nodeParsed.set(entry.getKey(), parsedValueNode);
            } else if (entry.getValue().isObject()) {
                ObjectNode parsedObjectNode = this.recurseGenerateFromTemplate((ObjectNode) entry.getValue());
                nodeParsed.set(entry.getKey(), parsedObjectNode);
            }
        });
        return nodeParsed;
    }

    public CompiledNodeContainer compileTemplate(ObjectNode templateNode) {
        CompiledNodeContainer rootContainer = CompiledNodeContainer.builder().build();
        templateNode.fields().forEachRemaining(entry -> {
            if (entry.getValue().isValueNode()) {
                IValueAccessor valueAccessor = valueAccessorParser.generateContext((ValueNode) entry.getValue());
                CompiledNodeField compiledField = CompiledNodeField.builder().valueAccessor(valueAccessor).build();
                rootContainer.getChilds().put(entry.getKey(), compiledField);
            } else if (entry.getValue().isObject()) {
                CompiledNodeContainer compiledContainer = this.compileTemplate((ObjectNode) entry.getValue());
                rootContainer.getChilds().put(entry.getKey(), compiledContainer);
            }
        });
        return rootContainer;
    }
}
