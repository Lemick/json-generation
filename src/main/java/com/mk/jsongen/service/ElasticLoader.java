package com.mk.jsongen.service;

import com.google.common.collect.Iterables;
import com.mk.jsongen.model.pojo.ElasticLoadRequest;
import com.mk.jsongen.model.pojo.ElasticLoadResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ElasticLoader {

    public static final int ELASTIC_BULK_BATCH_SIZE = 20000;

    /**
     * Batch INSERT documents using /_bulk API
     */
    public ElasticLoadResult loadIntoElastic(ElasticLoadRequest request, List<String> documents) throws IOException {
        try (RestHighLevelClient client = createRestClient(request)) {
            List<BulkResponse> bulkResponses = new ArrayList<>();
            for (List<String> documentsBatch : Iterables.partition(documents, ELASTIC_BULK_BATCH_SIZE)) {
                BulkRequest bulkRequest = processSingleBatch(request, client, documentsBatch);
                bulkResponses.add(client.bulk(bulkRequest, RequestOptions.DEFAULT));
            }
            return generateLoadResult(bulkResponses);
        }
    }

    private ElasticLoadResult generateLoadResult(List<BulkResponse> bulkResponses) {
        List<String> bulkErrors = bulkResponses.stream()
                .filter(BulkResponse::hasFailures)
                .map(BulkResponse::buildFailureMessage)
                .collect(Collectors.toList());
        return ElasticLoadResult.builder().errors(bulkErrors).build();
    }

    private BulkRequest processSingleBatch(ElasticLoadRequest request, RestHighLevelClient client, List<String> documentsBatch) throws IOException {
        log.info("Processing batch of " + documentsBatch.size() + " documents");
        BulkRequest bulkRequest = new BulkRequest();
        documentsBatch.forEach(serializedNode -> {
            IndexRequest indexRequest = new IndexRequest(request.getIndex()).source(serializedNode, XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        client.bulk(bulkRequest, RequestOptions.DEFAULT);
        return bulkRequest;
    }

    private RestHighLevelClient createRestClient(ElasticLoadRequest request) {
        HttpHost elasticNode = HttpHost.create(request.getElasticUrl());
        RestClientBuilder restClientBuilder = RestClient.builder(elasticNode);
        return new RestHighLevelClient(restClientBuilder);
    }
}
