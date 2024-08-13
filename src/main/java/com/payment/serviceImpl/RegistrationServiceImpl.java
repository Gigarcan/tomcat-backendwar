package com.payment.serviceImpl;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.payment.EmailService.EmailService;
import com.payment.EmailService.SmtpMailService;
import com.payment.entity.EmailOtpVerify;
import com.payment.entity.UsersTable;
import com.payment.repo.OtpRepo;
import com.payment.repo.RegistrationRepo;
import com.payment.service.RegistrationService;

import jakarta.mail.MessagingException;
@Service
public class RegistrationServiceImpl implements RegistrationService{
	
	
	@Autowired
	private RegistrationRepo repo;
	@Autowired
	private EmailService emailService;
	@Autowired
	private SmtpMailService smtpService;
	
	@Autowired
	private OtpRepo otpRepo;

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
			Calendar calendar = Calendar.getInstance();
	        calendar.add(Calendar.MINUTE, 5);
	        Date expirationTime = calendar.getTime();
	        data.setOtpExpiration(expirationTime);
//			String name="shashi kumar";
			//String email=emailService.sendEmailToVerifyOTP(data.getEmail(),otp, name);
//			String emailSent=emailService.sendEmailToVerifyOTP(data.getEmail(),otp, data.getFirstName()+" "+data.getLastName());
			try {
				smtpService.sendSimpleEmail(data.getEmail(),otp, data.getFirstName()+" "+data.getLastName());
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			UsersTable expiry=repo.save(data);
			return ResponseEntity.status(HttpStatus.OK).body(expiry.getOtpExpiration());
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
	    UsersTable data = repo.findByEmail(email);
	    
	    if (data != null) {
	        Date now = new Date();
	        Date otpExpiration = data.getOtpExpiration();
	        
	        if (otpExpiration == null || now.after(otpExpiration)) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("OTP has expired. Please request a new one.");
	        }
	        
	        if (data.getOtp().equals(otp)) {
	            return ResponseEntity.status(HttpStatus.OK).body("OTP verified successfully");
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OTP does not match");
	        }
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User details not exist!");
	    }
	}
	
	
	@Override
	public ResponseEntity<?> sendOTPPayment(String email) {
		EmailOtpVerify data=otpRepo.findByEmail(email);
		if(data!=null) {
			String otp=generateOTP();
			data.setOtp(otp);
			try {
				smtpService.sendSimpleEmailForPayment(email, otp);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			otpRepo.save(data);
			return ResponseEntity.status(HttpStatus.OK).body("otp sent and saved successfully");
		}else {
			String otp=generateOTP();
			EmailOtpVerify newone=new EmailOtpVerify();
			newone.setEmail(email);
			newone.setOtp(otp);
			try {
				smtpService.sendSimpleEmailForPayment(email, otp);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			otpRepo.save(newone);
			return ResponseEntity.status(HttpStatus.OK).body("new record inserted");
		}
	}
	
	@Override
	public ResponseEntity<?> verifyOTPPayment(String email, String otp) {
		EmailOtpVerify data = otpRepo.findByEmail(email);
	    
	    if (data != null) {
	        
	        if (data.getOtp().equals(otp)) {
	            return ResponseEntity.status(HttpStatus.OK).body("OTP verified successfully");
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("OTP does not match");
	        }
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("otp details not exist!");
	    }
	}

}
