package com.mk.jsongen.generator;

import com.mk.jsongen.generator.contract.IGenerator;
import com.mk.jsongen.utils.SecureRandomUtil;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoolGenerator implements IGenerator {

    @Override
    public Object generate() {
        return String.valueOf(SecureRandomUtil.randomBoolean());
    }
}
