package com.mk.jsongen.generator;

import com.mk.jsongen.generator.contract.IGenerator;
import com.mk.jsongen.utils.SecureRandomUtil;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class DateGenerator implements IGenerator {

    private Instant min;
    private Instant max;

    @Override
    public Object generate() {
        long minEpoch = min.getEpochSecond();
        long maxEpoch = max.getEpochSecond();
        long random = SecureRandomUtil.randomLong(minEpoch, maxEpoch);

        Instant generatedInstant = Instant.ofEpochSecond(random);
        return DateTimeFormatter.ISO_INSTANT.format(generatedInstant);
    }
}
