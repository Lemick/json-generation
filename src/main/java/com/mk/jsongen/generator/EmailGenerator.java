package com.mk.jsongen.generator;

import com.mk.jsongen.generator.contract.IGenerator;
import com.mk.jsongen.model.stock.StockFirstName;
import com.mk.jsongen.model.stock.StockLastName;
import com.mk.jsongen.model.stock.StockMailDomain;
import lombok.Builder;
import lombok.Data;

import static com.mk.jsongen.utils.SecureRandomUtil.randomElem;

@Data
@Builder
public class EmailGenerator implements IGenerator {

    @Override
    public Object generate() {

        String firstName = randomElem(StockLastName.LAST_NAMES).substring(0, 1);
        String lastName = randomElem(StockFirstName.FIRST_NAMES);
        String domain = randomElem(StockMailDomain.MAIL_DOMAINS);
        return firstName + "." + lastName + "@" + domain;
    }
}
