package com.mk.jsongen.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@Builder
public class Function {

    private String functionName;
    private List<String> args;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Function function = (Function) o;
        return Objects.equals(functionName, function.functionName) &&
                Objects.equals(args, function.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(functionName, args);
    }
}
