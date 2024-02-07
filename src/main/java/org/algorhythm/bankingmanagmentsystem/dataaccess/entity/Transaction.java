package org.algorhythm.bankingmanagmentsystem.dataaccess.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private double amount;
    private LocalDateTime timestamp;
    private String type;

    @ManyToOne
    @JsonIgnore
    private Account account;

    public Transaction(double amount, LocalDateTime timestamp, String type, Account account) {
        this.amount = amount;
        this.timestamp = timestamp;
        this.type = type;
        this.account = account;
    }
}
