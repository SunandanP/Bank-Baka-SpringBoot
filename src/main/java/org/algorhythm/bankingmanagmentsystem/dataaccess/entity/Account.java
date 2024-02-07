package org.algorhythm.bankingmanagmentsystem.dataaccess.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@Setter
@Getter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private double minBalance;
    private String name;
    private double dailyLimit;
    @JsonIgnore
    private String password;
    private int age;
    private String IFSC;
    private double balance;
    private  String PAN;
    private  String aadhar;
    private  String mobile;
    private String addr;
    private String accountType;
    private String accountNum;

    public Account(long id, double minBalance, double dailyLimit, String name, String password, int age, String IFSC, double balance, String PAN, String aadhar, String mobile, String addr, String accountType, String accountNum) {
        this.id = id;
        this.name = name;
        this.dailyLimit = dailyLimit;
        this.minBalance = minBalance;
        this.password = password;
        this.age = age;
        this.IFSC = IFSC;
        this.balance = balance;
        this.PAN = PAN;
        this.aadhar = aadhar;
        this.mobile = mobile;
        this.addr = addr;
        this.accountType = accountType;
        this.accountNum = accountNum;




    }
}
