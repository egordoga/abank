package com.abank.task.repository;

import com.abank.task.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {
    Client findClientByFirstNameAndLastName(String firstName, String lastName);
}
