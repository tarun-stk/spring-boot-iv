package com.stk.transaction.controller;

import com.stk.transaction.dto.FlightBookingAcknowledgement;
import com.stk.transaction.dto.FlightBookingRequest;
import com.stk.transaction.service.FlightBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flights")
public class FlightBookingController {
    @Autowired
    private FlightBookingService service;


    @PostMapping("/book")
    public FlightBookingAcknowledgement bookFlightTicket(@RequestBody FlightBookingRequest request){
        return service.bookFlightTicket(request);
    }

}
