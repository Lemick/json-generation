package com.mk.jsongen.generator;

import com.mk.jsongen.generator.contract.IGenerator;
import lombok.Builder;
import lombok.Data;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

@Data
@Builder
public class JavascriptEvalGenerator implements IGenerator {

    private String body;

    @Override
    public Object generate() {
        try (Context context = Context.newBuilder().allowAllAccess(false).build()) {
            String scriptJs = "(function()" + body + ").call()";
            Value value = context.eval("js", scriptJs);
            return value.as(Object.class);
        }
    }
}
