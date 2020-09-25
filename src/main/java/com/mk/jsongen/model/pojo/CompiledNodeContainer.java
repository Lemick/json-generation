package com.mk.jsongen.model.pojo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CompiledNodeContainer implements ICompiledNode {

    private Map<String, ICompiledNode> childs;

    @Builder
    public CompiledNodeContainer(Map<String, ICompiledNode> childs) {
        this.childs = (childs == null) ? new HashMap<>() : childs;
    }

    @Override
    public boolean isGroup() {
        return true;
    }

    @Override
    public JsonNode toTreeNode() {
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        childs.forEach((k, v) -> objectNode.set(k, v.toTreeNode()));
        return objectNode;
    }
}
