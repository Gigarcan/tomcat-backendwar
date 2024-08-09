package com.payment.serviceImpl;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.payment.EmailService.EmailService;
import com.payment.entity.UsersTable;
import com.payment.repo.RegistrationRepo;
import com.payment.service.RegistrationService;
@Service
public class RegistrationServiceImpl implements RegistrationService{
	
	@Autowired
	private RegistrationRepo repo;
	@Autowired
	private EmailService emailService;

	@Override
	public ResponseEntity<?> saveRegistration(UsersTable model) {
		UsersTable data=repo.findByPhoneNumberAndEmail(model.getPhoneNumber(),model.getEmail());
		if(data==null) {
			model=repo.save(model);
			return ResponseEntity.status(HttpStatus.CREATED).body(model);
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Details Allready Exist!");
		}
	}

	@Override
	public ResponseEntity<?> sendOTP(String email) {
		UsersTable data=repo.findByEmail(email);
		if(data!=null) {
			String otp=generateOTP();
			data.setOtp(otp);
			String name="shashi kumar";
			//String email=emailService.sendEmailToVerifyOTP(data.getEmail(),otp, name);
			String emailSent=emailService.sendEmailToVerifyOTP(data.getEmail(),otp, data.getFirstName()+" "+data.getLastName());
			repo.save(data);
			return ResponseEntity.status(HttpStatus.OK).body("Please check your email to verify the OTP.");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User details not Exist!");
	}
	
	private String generateOTP() {
		Random r = new Random();
		int random = r.nextInt(9000) + 100000;
		StringBuilder sb = new StringBuilder();
		sb.append(random);
		return sb.toString();
	}
	@Override
	public ResponseEntity<?> verifyOTP(String email, String otp) {
		UsersTable data=repo.findByEmail(email);
		if(data != null) {
			if(data.getOtp().equals(otp)){
				return ResponseEntity.status(HttpStatus.OK).body("Otp Verified successfully");
			}else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Otp Does not match");
			}
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User details not Exist!");
		}
	}
}
