package com.payment.service;

import org.springframework.http.ResponseEntity;

import com.payment.entity.UsersTable;

public interface RegistrationService {
	public ResponseEntity<?> saveRegistration(UsersTable model);

	public ResponseEntity<?> sendOTP(String phoneNmr);
	public ResponseEntity<?> verifyOTP(String email, String otp);
}
