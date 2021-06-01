package com.abank.task.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionRespDto {
    private String code;
    private String text;

    public ExceptionRespDto(String code, String text) {
        this.code = code;
        this.text = text;
    }
}
