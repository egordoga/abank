package com.abank.task.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;
    private String reason;

    @ManyToOne
    @JoinColumn(name = "source_acc_id")
    private Account sourceAcc;

    @ManyToOne
    @JoinColumn(name = "dest_acc_id")
    private Account destAcc;

    @Column(name = "trans_date")
    private LocalDateTime date;
}
