package com.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payment.EmailService.EmailService;
import com.payment.EmailService.SmtpMailService;
import com.payment.entity.UsersTable;
import com.payment.service.RegistrationService;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class RegistrationController {
	@Autowired
	private RegistrationService service;
	@Autowired
	private SmtpMailService smtpService;

	@PostMapping("/signUp")
	public ResponseEntity<?> saveRegistration(@RequestBody UsersTable newOne) {
		ResponseEntity<?> data= service.saveRegistration(newOne);
		return data;
	}
	@PostMapping("/verifyOTP/{email}/{otp}")
	public ResponseEntity<?> verifyOTP(@PathVariable String email,@PathVariable String otp) {
		ResponseEntity<?> data= service.verifyOTP(email,otp);
		return data;
	}
	
	@GetMapping("/sendOTP/{email}")
	public ResponseEntity<?> sendOTP(@PathVariable String email) {
		ResponseEntity<?> data= service.sendOTPPayment(email);
		return data;
	}
}
