package com.mk.jsongen.generator;

import com.mk.jsongen.generator.contract.IGenerator;
import com.mk.jsongen.utils.SecureRandomUtil;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PhoneGenerator implements IGenerator {

    @Override
    public Object generate() {
        StringBuilder sb = new StringBuilder();
        sb.append("+").append(SecureRandomUtil.randomInt(1, 999));
        for (int i = 0; i < 9; i++) {
            sb.append(SecureRandomUtil.randomInt(0, 9));
        }
        return sb.toString();
    }
}
