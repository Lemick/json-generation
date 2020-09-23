package com.mk.jsongen.service;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JsonTemplateGenerator {

    @Autowired
    TemplateParser templateParser;

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
}
