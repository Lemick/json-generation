package com.mk.jsongen.facade;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mk.jsongen.model.pojo.ElasticLoadRequest;
import com.mk.jsongen.service.BatchTemplateTransformer;
import com.mk.jsongen.service.ElasticLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * Helper Controller capable of generating JSON docs and insert them in Elastic with Bulk API
 */
@RestController
@Validated
@Slf4j
public class ElasticDocLoaderFacade {

    @Autowired
    BatchTemplateTransformer batchTemplateTransformer;

    @Autowired
    ElasticLoader elasticLoader;

    @PostMapping("/generate/to/elastic")
    public Object generateIntoElastic(@RequestBody @Valid ElasticLoadRequest request) throws IOException {
        List<String> generatedDocuments = batchTemplateTransformer.batchCreateSerialized(request.getDocCount(), request.getTemplate());
        return elasticLoader.loadIntoElastic(request, generatedDocuments);
    }

}
