package com.grupo1.util;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService{

    private  final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public Boolean SendEmail(String to, String subject, String body) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@hosp.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);

            return true;
        }catch(Exception e){
            return false;
        }
    }
}

