package com.abank.task.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ClientDto {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    public ClientDto(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
