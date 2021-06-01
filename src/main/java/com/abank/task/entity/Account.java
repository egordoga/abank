package com.abank.task.entity;

import com.abank.task.model.AccountType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("account_id")
    private Long id;

    @JsonProperty("account_num")
    private String accountNum;
    // Тут можно было поставить простой @Enumerated (EnumType.STRING), но он ест много места в базе
    // а т.к. у нас база банковская т.е. большая, жалеем ее и применяем конвертер
    @JsonProperty("account_type")
    private AccountType accountType;
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonBackReference("client")
    private Client client;

    @OneToMany(mappedBy = "destAcc", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Payment> paymentDestList;

    @OneToMany(mappedBy = "sourceAcc", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Payment> paymentSourceList;
}
