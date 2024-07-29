package com.stk.microservice2.service;

import com.stk.microservice2.client.ProductClient;
import com.stk.microservice2.dto.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {
    private final ProductClient productClient;
    public Product getProductById(Integer id){
        try{
            return productClient.getProductById(id).getBody();
        }
        catch (Exception exception){
            log.error("exception ex: {}", exception.getMessage());
        }

        return null;
    }
}
