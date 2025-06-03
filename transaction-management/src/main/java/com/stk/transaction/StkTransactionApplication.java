package com.stk.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class StkTransactionApplication {

	public static void main(String[] args) {
		SpringApplication.run(StkTransactionApplication.class, args);
	}

}
