package com.abank.task.model;

import com.fasterxml.jackson.annotation.JsonValue;

// Хотя в задании только один тип счета, явно предполагается, что их будет больше
public enum AccountType {
    CARD_SIMPLE("card/simple");

    private final String code;

    AccountType(String code) {
        this.code = code;
    }

    @JsonValue
    public String getCode() {
        return code;
    }
}
