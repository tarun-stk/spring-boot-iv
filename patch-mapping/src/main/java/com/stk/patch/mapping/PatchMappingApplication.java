package com.stk.patch.mapping;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
@RequiredArgsConstructor
public class PatchMappingApplication implements CommandLineRunner{

	private final ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(PatchMappingApplication.class, args);
	}

	@Override
	public void run(String ... args){
		productRepository.saveAll(Stream.of(new Product(1, "ball", 50.0),
				new Product(2, "bat", 1563.0),
				new Product(3, "wickets", 499.0),
				new Product(4, "bails", 10000.0))
				.collect(Collectors.toList()));
	}

}
