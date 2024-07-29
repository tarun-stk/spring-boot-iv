package com.stk.microservice1.service;

import com.stk.microservice1.dto.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductService {

    public Product getProductById(Integer id){
        log.info("Inside callee microservice");
        return Product.builder().id(1).name("bottle").cost(10.9).build();
    }
}
