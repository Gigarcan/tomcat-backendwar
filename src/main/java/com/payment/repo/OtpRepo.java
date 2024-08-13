package com.payment.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payment.entity.EmailOtpVerify;

public interface OtpRepo extends JpaRepository<EmailOtpVerify, Integer>{
	EmailOtpVerify findByEmail(String email);

}
