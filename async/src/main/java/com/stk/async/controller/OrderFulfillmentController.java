package com.stk.async.controller;

import com.stk.async.dto.Order;
import com.stk.async.service.OrderFulfillmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderFulfillmentController {


    @Autowired
    private OrderFulfillmentService service;

    /*URL: http://localhost:8080/orders
    * body
    * {
    "productId": 1,
    "name": "Mobile",
    "productType": "Electronics",
    "qty": 10,
    "price": 10000.0
}*/
    @PostMapping
    public ResponseEntity<Order> processOrder(@RequestBody Order order) throws InterruptedException {
        service.processOrder(order); // synchronous
        /*All the below will be executed asynchronously, means once synchronous commn is done
         * response will return from this method, and below methods will be keep on executing*/
        // asynchronous
        service.notifyUser(order);
        service.assignVendor(order);
        service.packaging(order);
        service.assignDeliveryPartner(order);
        service.assignTrailerAndDispatch(order);
        return ResponseEntity.ok(order);
    }

    /*Response w/o using async: took 26secs

    * with using async took only 2sec
    logs: async methods going thru diff threads
    2024-07-16T16:55:15.104+05:30  INFO 20060 --- [async] [nio-8080-exec-3] com.stk.async.service.PaymentService     : initiate payment for order 1
2024-07-16T16:55:17.112+05:30  INFO 20060 --- [async] [nio-8080-exec-3] com.stk.async.service.PaymentService     : completed payment for order 1
2024-07-16T16:55:19.127+05:30  INFO 20060 --- [async] [yncTaskThread-3] c.s.a.service.OrderFulfillmentService    : Order packaging completed AsyncTaskThread-3
2024-07-16T16:55:21.122+05:30  INFO 20060 --- [async] [yncTaskThread-1] c.s.a.service.OrderFulfillmentService    : Notified to the user AsyncTaskThread-1
2024-07-16T16:55:22.117+05:30  INFO 20060 --- [async] [yncTaskThread-2] c.s.a.service.OrderFulfillmentService    : Assign order to vendor AsyncTaskThread-2
2024-07-16T16:55:22.133+05:30  INFO 20060 --- [async] [yncTaskThread-3] c.s.a.service.OrderFulfillmentService    : Trailer assigned and Order dispatched AsyncTaskThread-3
2024-07-16T16:55:27.126+05:30  INFO 20060 --- [async] [yncTaskThread-4] c.s.a.service.OrderFulfillmentService    : Delivery partner assigned AsyncTaskThread-4
*/
}