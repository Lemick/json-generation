package com.mk.jsongen.generator;

import com.mk.jsongen.generator.contract.IGenerator;
import com.mk.jsongen.utils.SecureRandomUtil;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IntGenerator implements IGenerator {

    private int min;
    private int max;

    @Override
    public Object generate() {
        return SecureRandomUtil.randomInt(min, max);
    }
}
