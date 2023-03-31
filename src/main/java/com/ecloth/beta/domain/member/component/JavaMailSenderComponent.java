package com.ecloth.beta.domain.member.component;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JavaMailSenderComponent {

    private final JavaMailSender javaMailSender;
    public void sendMail(String email, String subject, String content){

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject(subject);
        msg.setText(content);

        javaMailSender.send(msg);
    }

}
