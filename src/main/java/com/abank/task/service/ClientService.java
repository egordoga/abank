package com.abank.task.service;

import com.abank.task.entity.Client;
import com.abank.task.repository.ClientRepo;
import org.springframework.stereotype.Service;

@Service
public class ClientService implements IClientService{

    private final ClientRepo clientRepo;

    public ClientService(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Override
    public Long addClient(Client client) {
        // В данном случае сравниваем только имя и фамилию. Считаем, что однофамильцев не существует т.к. других данных нет
        Client clientFromDb = clientRepo.findClientByFirstNameAndLastName(client.getFirstName(), client.getLastName());
        if (clientFromDb != null) {
            return -1L;
        }
        return clientRepo.save(client).getId();
        // Не знаю может ли быть ситуация, когда номер счета оператор как-то вводит сам. Здесь считаю, что этот номер
        // как-то автоматом генерится на клиенте. Иначе нужна аналогичная проверка на наличие такого номера в базе.
    }
}
