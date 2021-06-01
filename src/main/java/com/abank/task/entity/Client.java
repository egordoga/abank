package com.abank.task.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;

    // Обычно в банковских БД данные не удаляются, поэтому ставлю CascadeType.ALL. Иначе надо будет думать какой лучше cascade
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "client")
    private List<Account> accounts;


}
