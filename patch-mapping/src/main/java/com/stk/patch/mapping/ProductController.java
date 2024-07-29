package com.stk.patch.mapping;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /*URL: http://localhost:8080/products/4
    * body: {
    "cost": 1001.0
}*/
    @PatchMapping("/{id}")
    public ResponseEntity<Product> partialUpdateProduct(@PathVariable("id") Integer productId, @RequestBody Map<String, Object> body) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.partialUpdateProduct(productId, body));
    }

    /*Before sending patch request:
    * COST  	ID  	NAME
50.0	1	ball
1563.0	2	bat
499.0	3	wickets
10000.0	4	bails


After sending patch request:

    COST  	ID  	NAME
50.0	1	ball
1563.0	2	bat
499.0	3	wickets
1001.0	4	bails

     */
}
