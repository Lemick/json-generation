package com.mk.jsongen.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mk.jsongen.model.pojo.CompiledNodeContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BatchTemplateTransformer {

    @Autowired
    JsonTemplateGenerator templateGenerator;

    @Autowired
    ObjectMapper objectMapper;

    public List<JsonNode> generateJsonNodes(int size, ObjectNode jsonTemplate) {
        CompiledNodeContainer compiledTemplate = templateGenerator.compileTemplate(jsonTemplate);
        return IntStream.range(0, size)
                .mapToObj(v -> compiledTemplate.toTreeNode())
                .collect(Collectors.toList());
    }

    public List<String> generateSerializedJsonNodes(int size, ObjectNode jsonTemplate) throws JsonProcessingException {
        List<JsonNode> jsonNodes = generateJsonNodes(size , jsonTemplate);
        List<String> serializedJsonNodes = new ArrayList<>(jsonNodes.size());
        for(JsonNode jsonNode: jsonNodes) {
            serializedJsonNodes.add(objectMapper.writeValueAsString(jsonNode));
        }
        return serializedJsonNodes;
    }

}
