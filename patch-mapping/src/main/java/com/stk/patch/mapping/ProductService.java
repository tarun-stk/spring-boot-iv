package com.stk.patch.mapping;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    public Product partialUpdateProduct(Integer productId, Map<String, Object> body){
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("No product found"));
        body.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Product.class, key);
            field.setAccessible(true);
            ReflectionUtils.setField(field, product, value);
        });

        return productRepository.save(product);
    }
}
