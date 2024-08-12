package com.payment.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.payment.entity.Order;
import com.payment.serviceImpl.OrderService;
import com.razorpay.RazorpayException;

@RestController
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/createOrder")
    public ResponseEntity<Order> createOrder(@RequestBody Order orders) throws RazorpayException {
        Order razorpayOrder = orderService.createOrder(orders);
        return new ResponseEntity<>(razorpayOrder, HttpStatus.CREATED);
    }

    @PostMapping("/paymentCallback")
    public ResponseEntity<?> paymentCallback(@RequestBody Map<String, String> response) {
        orderService.updateStatus(response);
        return ResponseEntity.ok().body("Payment successful");
    }

}
