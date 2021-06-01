package com.abank.task.service;

import com.abank.task.entity.Account;
import com.abank.task.entity.Payment;
import com.abank.task.model.dto.ParameterSetDto;
import com.abank.task.model.dto.PaymentDto;
import com.abank.task.model.dto.ReportRequestDto;
import com.abank.task.repository.AccountRepo;
import com.abank.task.repository.PaymentRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService implements IPaymentService {
    private final AccountRepo accountRepo;
    private final PaymentRepo paymentRepo;

    public PaymentService(AccountRepo accountRepo, PaymentRepo paymentRepo) {
        this.accountRepo = accountRepo;
        this.paymentRepo = paymentRepo;
    }

    @Override
    @Transactional()
    public Long addPayment(PaymentDto paymentDto) {
        Optional<Account> optSourceAcc = accountRepo.findById(paymentDto.getSourceAccId());
        if (optSourceAcc.isEmpty()) {
            return -1L;
        }

        Optional<Account> optDestAcc = accountRepo.findById(paymentDto.getDestAccId());
        if (optDestAcc.isEmpty()) {
            return -2L;
        }
        Account sourceAcc = optSourceAcc.get();
        Account destAcc = optDestAcc.get();

        if (sourceAcc.getBalance().compareTo(paymentDto.getAmount()) < 0) {
            return -3L;
        }

        Payment payment = new Payment();
        sourceAcc.setBalance(sourceAcc.getBalance().subtract(paymentDto.getAmount()));
        destAcc.setBalance(destAcc.getBalance().add(paymentDto.getAmount()));
        paymentDto.toObject(payment);
        payment.setDate(LocalDateTime.now());
        payment.setSourceAcc(sourceAcc);
        payment.setDestAcc(destAcc);
        accountRepo.save(sourceAcc);
        accountRepo.save(destAcc);
        return paymentRepo.save(payment).getId();
    }

    @Override
    public List<ReportRequestDto> makeReport(ParameterSetDto setDto) {
        List<ReportRequestDto> reportRequestDtoList = new ArrayList<>();
        ReportRequestDto dto = new ReportRequestDto();
        List<Payment> report = paymentRepo.findAllByDestAcc_IdAndSourceAcc_IdAndDestAcc_Client_IdAndSourceAcc_Client_Id(
                setDto.getDestAccId(), setDto.getSourceAccId(), setDto.getRecipientId(), setDto.getPayerId());

        if (report.isEmpty()) {
            return null;
        }
        for (Payment payment : report) {
            reportRequestDtoList.add(dto.toDto(payment));
        }
        return reportRequestDtoList;
    }
}
