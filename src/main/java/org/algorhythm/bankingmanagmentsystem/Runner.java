package org.algorhythm.bankingmanagmentsystem;

import org.algorhythm.bankingmanagmentsystem.dataaccess.entity.Account;
import org.algorhythm.bankingmanagmentsystem.dataaccess.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        accountRepository.save(new Account(1l, 1500.0d, 10000.0d, "Sunandan", "pass", 20, "IFSC", 30000, "PAN CARD", "AADHAR CARD", "9004138024", "Pune", "Current", "1010010101"));
    }
}
