package com.abank.task.model.dto;

import com.abank.task.entity.Payment;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Setter
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportRequestDto {

    @JsonProperty("payment_id")
    private Long paymentId;

    @JsonProperty("timestamp")
    private String date;

    @JsonProperty("src_acc_id")
    private String srcAccNum;

    @JsonProperty("dest_acc_id")
    private String destAccNum;

    private BigDecimal amount;
    private ClientDto payer;
    private ClientDto recipient;

    public ReportRequestDto toDto(Payment payment) {
        if (payment != null) {
            paymentId = payment.getId();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            date = payment.getDate().format(formatter);
            srcAccNum = payment.getSourceAcc().getAccountNum();
            destAccNum = payment.getDestAcc().getAccountNum();
            amount = payment.getAmount();
            payer = new ClientDto(payment.getSourceAcc().getClient().getFirstName(), payment.getSourceAcc().getClient().getLastName());
            recipient = new ClientDto(payment.getDestAcc().getClient().getFirstName(), payment.getDestAcc().getClient().getLastName());
        }
        return this;
    }
}
