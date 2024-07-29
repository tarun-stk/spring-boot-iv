package com.stk.microservice1.dto;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
public class Product {
    private int id;
    private String name;
    private double cost;
}
