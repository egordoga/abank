package com.abank.task.model;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class AccountTypeConverter implements AttributeConverter<AccountType, String> {
    @Override
    public String convertToDatabaseColumn(AccountType accountType) {
        if (accountType == null) {
            return null;
        }
        return accountType.getCode();
    }

    @Override
    public AccountType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        return Stream.of(AccountType.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
