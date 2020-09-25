package com.mk.jsongen.model.pojo;

import com.fasterxml.jackson.databind.JsonNode;
import com.mk.jsongen.model.pojo.accessor.IValueAccessor;

public interface ICompiledNode {

    boolean isGroup();
    JsonNode toTreeNode();
}
