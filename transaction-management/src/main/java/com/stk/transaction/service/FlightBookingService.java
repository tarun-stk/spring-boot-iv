package com.stk.transaction.service;

import com.stk.transaction.dto.FlightBookingAcknowledgement;
import com.stk.transaction.dto.FlightBookingRequest;
import com.stk.transaction.entity.PassengerInfo;
import com.stk.transaction.entity.PaymentInfo;
import com.stk.transaction.repo.PassengerInfoRepository;
import com.stk.transaction.repo.PaymentInfoRepository;
import com.stk.transaction.utils.PaymentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class FlightBookingService {

    @Autowired
    private PassengerInfoRepository passengerInfoRepository;
    @Autowired
    private PaymentInfoRepository paymentInfoRepository;


    /*Without using below anno, the passengerInfo will still be saved because we're saving it before payment exeuction
     * Actually payment is throwing exception and those details are nto being stored
     * in that scenario we've to not store passenger details also, as payment was not successful
     * if we use below anno, it will ensure that records will be inserted only when all the operations are successful
     *  in below method, all db scripts will be executed in form of a transaction, if anything goes wrong they will
     * be rolled back immediately */

    /*below output from console when not using anno
    * Hibernate: select next value for passenger_infos_seq
Hibernate: insert into passenger_infos (destination,arrival_time,email,fare,name,pickup_time,source,travel_date,p_id) values (?,?,?,?,?,?,?,?,?)*/

    /*below output from console when using anno
     * Hibernate: select next value for passenger_infos_seq
     * No insert statement found*/

    /*To generate exception use below body in postman post request
    Request URL: http://localhost:8080/flights/book
    * {
    "passengerInfo":{
            "name": "Tarun",
            "email": "tarun@gmail.com",
            "source": "Delhi",
            "Destination": "Buenos Aires",
            "travelDate": "20-11-2023",
            "pickupTime": "10:30AM",
            "arrivalTime": "10:30AM",
            "fare": 56000
    },
    "paymentInfo":{
        "accountNo": "acc1",
        "cardType": "Credit"
    }

}
*
*
*   Use below body to not generate any exception, and record will be inserted into db
* {
    "passengerInfo":{
            "name": "Tarun",
            "email": "tarun@gmail.com",
            "source": "Delhi",
            "Destination": "Buenos Aires",
            "travelDate": "20-11-2023",
            "pickupTime": "10:30AM",
            "arrivalTime": "10:30AM",
            "fare": 5000
    },
    "paymentInfo":{
        "accountNo": "acc1",
        "cardType": "Credit"
    }

}*/
    @Transactional//(readOnly = false,isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public FlightBookingAcknowledgement bookFlightTicket(FlightBookingRequest request) {

        PassengerInfo passengerInfo = request.getPassengerInfo();
        passengerInfo = passengerInfoRepository.save(passengerInfo);

        PaymentInfo paymentInfo = request.getPaymentInfo();

        PaymentUtils.validateCreditLimit(paymentInfo.getAccountNo(), passengerInfo.getFare());

        paymentInfo.setPassengerId(passengerInfo.getPId());
        paymentInfo.setAmount(passengerInfo.getFare());
        paymentInfoRepository.save(paymentInfo);
        return new FlightBookingAcknowledgement("SUCCESS", passengerInfo.getFare(), UUID.randomUUID().toString().split("-")[0], passengerInfo);

    }
}
