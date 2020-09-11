package com.mk.jsongen.facade;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mk.jsongen.service.JsonTemplateGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class GeneratorFacade {

    @Autowired
    JsonTemplateGenerator templateGenerator;

    @PostMapping("/generate")
    public List<ObjectNode> generateFromTemplate(@RequestParam(name = "size", defaultValue = "10") int size, @RequestBody ObjectNode jsonTemplate) {
        List<ObjectNode> objectNodes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            ObjectNode jsonGenerated = templateGenerator.recurseGenerateFromTemplate(jsonTemplate);
            objectNodes.add(jsonGenerated);
        }
        return objectNodes;
    }

}
