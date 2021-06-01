package com.abank.task.service;

import com.abank.task.model.dto.ParameterSetDto;
import com.abank.task.model.dto.PaymentDto;
import com.abank.task.model.dto.ReportRequestDto;

import java.util.List;

public interface IPaymentService {
    Long addPayment(PaymentDto payment);
    List<ReportRequestDto> makeReport(ParameterSetDto setDto);
}
