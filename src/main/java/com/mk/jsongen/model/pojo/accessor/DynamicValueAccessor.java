package com.mk.jsongen.model.pojo.accessor;

import com.mk.jsongen.generator.contract.IGenerator;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.regex.Pattern;

@Data
@Builder
public class DynamicValueAccessor implements IValueAccessor {

    private final IGenerator generator;

    @Override
    public Object accessValue() {
        return generator.generate();
    }
}
