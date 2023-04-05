package com.ecloth.beta.domain.member.component;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@RequiredArgsConstructor
public class JavaMailSenderComponent {

    private final JavaMailSender javaMailSender;
    public void sendMail(String email, String subject, String content) throws MessagingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject(subject);
        String htmlContent = "<html><body>" + content + "</body></html>";
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }

}
