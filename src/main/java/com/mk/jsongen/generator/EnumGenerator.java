package com.mk.jsongen.generator;

import com.mk.jsongen.generator.contract.IGenerator;
import com.mk.jsongen.utils.SecureRandomUtil;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EnumGenerator implements IGenerator {

    List<String> values;

    @Override
    public Object generate() {
        return SecureRandomUtil.randomElem(values);
    }
}
