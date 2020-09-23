package com.mk.jsongen.generator;

import com.mk.jsongen.generator.contract.IGenerator;
import com.mk.jsongen.utils.SecureRandomUtil;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class CurrentInstantGenerator implements IGenerator {

    @Override
    public Object generate() {
        return DateTimeFormatter.ISO_INSTANT.format(Instant.now());
    }
}
