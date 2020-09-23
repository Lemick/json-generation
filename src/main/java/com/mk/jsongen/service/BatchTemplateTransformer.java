package com.mk.jsongen.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BatchTemplateTransformer {

    @Autowired
    JsonTemplateGenerator templateGenerator;

    @Autowired
    ObjectMapper objectMapper;

    public List<ObjectNode> batchCreate(int size, ObjectNode jsonTemplate) {
        List<ObjectNode> objectNodes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ObjectNode jsonGenerated = templateGenerator.recurseGenerateFromTemplate(jsonTemplate);
            objectNodes.add(jsonGenerated);
        }
        return objectNodes;
    }

    public List<String> batchCreateSerialized(int size, ObjectNode jsonTemplate) throws JsonProcessingException {
        List<String> serializedObjectNodes = new ArrayList<>();
        for (ObjectNode objectNode: batchCreate(size, jsonTemplate)) {
            String jsonGenerated =  objectMapper.writeValueAsString(objectNode);
            serializedObjectNodes.add(jsonGenerated);
        }
        return serializedObjectNodes;
    }
}
