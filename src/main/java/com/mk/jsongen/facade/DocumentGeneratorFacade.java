package com.mk.jsongen.facade;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mk.jsongen.service.BatchTemplateTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Slf4j
public class DocumentGeneratorFacade {

    @Autowired
    BatchTemplateTransformer batchTemplateTransformer;

    @PostMapping("/generate")
    public List<JsonNode> generateFromTemplate(
            @RequestParam(name = "size", defaultValue = "10") @Min(1) int size,
            @RequestBody @NotNull ObjectNode jsonTemplate) {

        return batchTemplateTransformer.generateJsonNodes(size, jsonTemplate);
    }

}
