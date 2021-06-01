package com.abank.task.model.dto;

import com.abank.task.entity.Payment;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
public class PaymentDto {
    private BigDecimal amount;
    private String reason;

    @JsonProperty("source_acc_id")
    private Long sourceAccId;

    @JsonProperty("dest_acc_id")
    private Long destAccId;

    public void toObject(Payment payment) {
        payment.setAmount(amount);
        payment.setReason(reason);
    }
}
