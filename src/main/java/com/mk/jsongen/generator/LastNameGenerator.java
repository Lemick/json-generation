package com.mk.jsongen.generator;

import com.mk.jsongen.generator.contract.IGenerator;
import com.mk.jsongen.model.stock.StockLastName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import static com.mk.jsongen.utils.SecureRandomUtil.randomElem;

@Data
@Builder
@AllArgsConstructor
public class LastNameGenerator implements IGenerator {


    @Override
    public Object generate() {
        return randomElem(StockLastName.LAST_NAMES);
    }
}
