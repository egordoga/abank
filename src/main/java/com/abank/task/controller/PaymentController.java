package com.abank.task.controller;

import com.abank.task.model.dto.*;
import com.abank.task.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay")
    public ResponseEntity<String> makePayment(@RequestBody String json, HttpServletRequest req) {
        ObjectMapper mapper;
        if (req.getContentType().contains("xml")) {
            mapper = new XmlMapper();
        } else {
            mapper = new ObjectMapper();
        }
        PaymentDto paymentDto;
        String response = "";
        ExceptionRespDto respDto;
        try {
            paymentDto = mapper.readValue(json, PaymentDto.class);
            mapper = new ObjectMapper();
            Long id = paymentService.addPayment(paymentDto);
            switch (id.intValue()) {
                case -1:
                    // Не понял из ТЗ какой код ошибки нужен в объекте ответа
                    respDto = new ExceptionRespDto("500", "Source account with id " +
                                                          paymentDto.getSourceAccId() + " not found");
                    response = mapper.writeValueAsString(respDto);
                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                case -2:
                    respDto = new ExceptionRespDto("500", "Destination account with id " +
                                                          paymentDto.getDestAccId() + " not found");
                    response = mapper.writeValueAsString(respDto);
                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
                case -3:
                    respDto = new ExceptionRespDto("500", "Not enough funds in the account with id " +
                                                          paymentDto.getSourceAccId());
                    response = mapper.writeValueAsString(respDto);
                    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            ObjectNode node = mapper.createObjectNode();
            node.put("payment_id", id);
            response = mapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            respDto = new ExceptionRespDto("400", "Ошибка парсинга запроса");
            try {
                response = mapper.writeValueAsString(respDto);
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
            }
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/pack")
    public ResponseEntity<String> makePayments(@RequestBody String json, HttpServletRequest req) {
        // Тут не понятно, какой id надо вернуть со статусом "error", поэтому возвращаю < 0
        ObjectMapper mapper;
        if (req.getContentType().contains("xml")) {
            mapper = new XmlMapper();
        } else {
            mapper = new ObjectMapper();
        }
        List<PaymentDto> payments;
        String response = "";
        List<ExceptionRespPackDto> respPackDtoList = new ArrayList<>();

        CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, PaymentDto.class);
        try {
            payments = mapper.readValue(json, type);
            mapper = new ObjectMapper();
            for (PaymentDto payment : payments) {
                Long id = paymentService.addPayment(payment);
                if (id < 0) {
                    respPackDtoList.add(new ExceptionRespPackDto(id, "error"));
                } else {
                    respPackDtoList.add(new ExceptionRespPackDto(id, "ok"));
                }
            }
            response = mapper.writeValueAsString(respPackDtoList);
        } catch (JsonProcessingException e) {
            ExceptionRespDto respDto = new ExceptionRespDto("400", "Ошибка парсинга запроса");
            try {
                response = mapper.writeValueAsString(respDto);
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
            }
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/report")
    public ResponseEntity<String> makeReport(@RequestBody String json, HttpServletRequest req) {
        // Тут совсем не понятно из ТЗ какие параметры приходят: все ли обязательны, source_acc должен соответствовать payer и т.п.
        // Считаю, что все параметры обязательны и соответствуют одному платежу.
        // Если это не так, и параметр может отсутствовать, а остальные параметры соответствуют разным платежам, то тут нужно
        // создавать запрос вроде этого
        //
        // select p.id, p.trans_date, a2.account_num as sacc, a.account_num as dacc, p.amount,
        //         c2.first_name as pfn, c2.last_name as pln, c.first_name as rfn, c.last_name as rln
        // from payment p
        // inner join account a on p.dest_acc_id = a.id
        // inner join account a2 on p.source_acc_id = a2.id
        // inner join client c on a.client_id = c.id
        // inner join client c2 on a2.client_id = c2.id
        // where p.dest_acc_id = 3 and p.source_acc_id = 1 and c.id = 2 and c2.id = 1
        //
        // и динамически собирать строку запроса, исходя из наличия параметров
        ObjectMapper mapper;
        if (req.getContentType().contains("xml")) {
            mapper = new XmlMapper();
        } else {
            mapper = new ObjectMapper();
        }
        ParameterSetDto setDto;
        String response = "";
        try {
            setDto = mapper.readValue(json, ParameterSetDto.class);
            mapper = new ObjectMapper();
            List<ReportRequestDto> list = paymentService.makeReport(setDto);
            if (list == null) {
                ExceptionRespDto respDto = new ExceptionRespDto("500", "Не найдено платежа по таким параметрам");
                response = mapper.writeValueAsString(respDto);
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            response = mapper.writeValueAsString(list);
        } catch (JsonProcessingException e) {
            ExceptionRespDto respDto = new ExceptionRespDto("400", "Ошибка парсинга запроса");
            try {
                response = mapper.writeValueAsString(respDto);
            } catch (JsonProcessingException jsonProcessingException) {
                jsonProcessingException.printStackTrace();
            }
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
