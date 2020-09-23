package com.mk.jsongen.model.pojo;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ElasticLoadRequest {

    @URL
    private String elasticUrl;

    @NotEmpty
    private String index;

    @Min(1)
    private int docCount;

    @NotNull
    private ObjectNode template;
}
