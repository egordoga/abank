package com.abank.task.controller;

import com.abank.task.entity.Client;
import com.abank.task.model.dto.ExceptionRespDto;
import com.abank.task.service.ClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/clients")
public class ClientController {


    private final ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> addClient(@RequestBody String json, HttpServletRequest req) {
        ObjectMapper mapper;
        ExceptionRespDto respDto;
        if (req.getContentType().contains("xml")) {
            mapper = new XmlMapper();
        } else {
            mapper = new ObjectMapper();
        }
        Client client;
        String response = "";
        try {
            client = mapper.readValue(json, Client.class);
            mapper = new ObjectMapper();
            Long clientId = service.addClient(client);
            if (clientId == -1) {
                respDto = new ExceptionRespDto("400", "Такой клиент уже есть в БД");
                response = mapper.writeValueAsString(respDto);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            // По ТЗ ответ слишком прост. Нет необходимости создавать отдельный объект для ответа
            ObjectNode node = mapper.createObjectNode();
            node.put("client_id", clientId);
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
}
