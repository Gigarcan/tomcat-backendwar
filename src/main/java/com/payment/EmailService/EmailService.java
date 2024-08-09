package com.payment.EmailService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;


@Service
public class EmailService {
	private final Logger logger = LoggerFactory.getLogger(EmailService.class);
	@Value("${sendgrid.api.key}")
	private String sendGridAPIKey;
 
	@Value("${email.send.fromEmail}")
	private String from;
	
	public String sendEmailToVerifyOTP(String emailId, String otp, String name) {
		String to = emailId;
		String subject = "Admin-Metierks";
		String body = "<html><body> Dear "+name+",<br/><br/>"
				+ "Thank you for choosing <strong>Metierks PVT LTD!</strong> To ensure the security of your account, we need to verify your identity.<br/><br/>"
				+ "Your One-Time Password (OTP) for verification is: <strong>" + otp + "</strong><br/><br/>"
				+ "Please use this code within the next 5 minutes to complete the verification process.<br/><br/>"
				+ "If you did not request this OTP or need any assistance, please contact our support team immediately.<br/><br/>"
				+ "<br/><br/>" + "Thank you for your cooperation.</body></html>";
		try {
			Email fromEmail = new Email(from);
			Email toEmail = new Email(to);
			Content content = new Content("text/html", body);
			Mail mail = new Mail(fromEmail, subject, toEmail, content);
 
			SendGrid sendGrid = new SendGrid(sendGridAPIKey);
			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
 
			Response response = sendGrid.api(request);
 
			logger.info("Email sent successfully. Status Code: {}", response.getStatusCode());
			return "Email sent successfully. Status Code: " + response.getStatusCode();
		} catch (Exception e) {
			logger.error("Error sending email: {}", e.getMessage(), e);
			return null;
		}
	}
}
