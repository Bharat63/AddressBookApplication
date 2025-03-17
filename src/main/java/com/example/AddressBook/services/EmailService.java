package com.example.AddressBook.services;

import com.example.AddressBook.serviceInterfaces.UserInterface;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired

    private JavaMailSender mailSender;

    public void sendVerificationEmail(String email, String token) {
        String subject = "Verify your account";
        String verificationUrl = "http://localhost:8080/auth/verify?token=" + token;
        String message = "Click here to verify account:" + verificationUrl;

        SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setTo(email);
        emailMessage.setSubject(subject);
        emailMessage.setText(message);

        mailSender.send(emailMessage);
    }

    public void sendResetEmail(String email, String resetToken){
        try {
            String resetLink="http://localhost:8080/auth/reset-password?token="+resetToken;
            MimeMessage message=mailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setTo(email);
            helper.setSubject("reset password");
            helper.setText("<p>Click the link below to reset your password:</p>"
                    + "<p><a href=\"" + resetLink + "\">Reset Password</a></p>", true);
            mailSender.send(message);
        }catch (MessagingException e) {
            throw new RuntimeException("Error sending email: " + e.getMessage());
        }

    }
}