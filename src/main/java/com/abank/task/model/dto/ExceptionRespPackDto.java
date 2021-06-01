package com.abank.task.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionRespPackDto {

    @JsonProperty("payment_id")
    private Long paymentId;
    private String status;

    public ExceptionRespPackDto(Long paymentId, String status) {
        this.paymentId = paymentId;
        this.status = status;
    }
}
