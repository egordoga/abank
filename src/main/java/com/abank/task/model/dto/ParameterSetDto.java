package com.abank.task.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParameterSetDto {

    @JsonProperty("payer_id")
    private Long payerId;

    @JsonProperty("recipient_id")
    private Long recipientId;

    @JsonProperty("source_acc_id")
    private Long sourceAccId;

    @JsonProperty("dest_acc_id")
    private Long destAccId;
}
