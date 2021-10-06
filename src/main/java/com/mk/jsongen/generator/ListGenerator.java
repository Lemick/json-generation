package com.mk.jsongen.generator;

import com.mk.jsongen.generator.contract.IGenerator;
import com.mk.jsongen.utils.SecureRandomUtil;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class ListGenerator implements IGenerator {

    private List<Object> values;

    @Override
    public Object generate() {
        Set<Object> result = new HashSet<>();
        int numberOfPick = SecureRandomUtil.randomInt(1, values.size() - 1);
        for (int i = 0; i < numberOfPick; i++) {
            result.add(SecureRandomUtil.randomElem(values));
        }
        return result;
    }
}
