package com.payment.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class SmtpMailService {
	@Autowired
    private JavaMailSender javaMailSender;

    public void sendSimpleEmail(String emailId, String otp, String name) throws MessagingException{
    	String subject = "Admin-Metierks";
        String body = "<html><body> Dear " + name + ",<br/><br/>"
                + "Thank you for choosing <strong>Metierks PVT LTD!</strong> To ensure the security of your account, we need to verify your identity.<br/><br/>"
                + "Your One-Time Password (OTP) for verification is: <strong>" + otp + "</strong><br/><br/>"
                + "Please use this code within the next 5 minutes to complete the verification process.<br/><br/>"
                + "If you did not request this OTP or need any assistance, please contact our support team immediately.<br/><br/>"
                + "<br/><br/>Thank you for your cooperation.</body></html>";

        // Create a MimeMessage object
        MimeMessage message = javaMailSender.createMimeMessage();

        // Use MimeMessageHelper to set the properties of the email
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(emailId);
        helper.setSubject(subject);
        helper.setText(body, true);  // The 'true' flag indicates that this is an HTML email
        helper.setFrom("support@metierks.com");

        // Send the email
        javaMailSender.send(message);
    
    }
}
