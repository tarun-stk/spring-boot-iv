package com.stk.microservice1.controller;

import com.stk.microservice1.dto.Product;
import com.stk.microservice1.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ProductController {
    private final ProductService productService;
    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Integer productId){
        Product product = productService.getProductById(productId);
        log.info("product: {}", product);
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }
}
