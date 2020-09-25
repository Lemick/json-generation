package com.mk.jsongen.model.pojo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.mk.jsongen.model.pojo.accessor.IValueAccessor;
import lombok.*;

@Data
@Builder
public class CompiledNodeField implements ICompiledNode {

    private IValueAccessor valueAccessor;

    @Override
    public boolean isGroup() {
        return false;
    }

    @Override
    public JsonNode toTreeNode() {
        return JsonNodeFactory.instance.pojoNode(valueAccessor.accessValue());
    }
}
