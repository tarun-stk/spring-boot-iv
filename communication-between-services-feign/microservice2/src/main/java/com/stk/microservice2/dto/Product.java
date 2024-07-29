package com.stk.microservice2.dto;

import lombok.*;

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
