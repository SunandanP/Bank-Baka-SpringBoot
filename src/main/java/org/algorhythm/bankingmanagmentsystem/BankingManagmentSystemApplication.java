package org.algorhythm.bankingmanagmentsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankingManagmentSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingManagmentSystemApplication.class, args);
	}

}
