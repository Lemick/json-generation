package com.mk.jsongen.model.pojo.accessor;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class StaticValueAccessor implements IValueAccessor {

    private Object value;

    @Override
    public Object accessValue() {
        return value;
    }

}
