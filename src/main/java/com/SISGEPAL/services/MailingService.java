package com.SISGEPAL.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

@Service
public class MailingService {

    private static final String EMAIL_NEW_USER_CREDENTIALS
            = "html/send_credentials/new_user_credentials";
    private static final String EMAIL_UPDATED_EMAIL
            = "html/send_credentials/update_email";
    @Value("${mail.subject.new_user}")
    private String subject_new_user;
    @Value("${mail.subject.update_email}")
    private String subject_update_email;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;


    @Async
    public void sendCredentialEmail(String to, String name, String username,
                                    String password) throws MessagingException, IOException {

        final Context ctx = new Context();
        ctx.setVariable("username", username);
        ctx.setVariable("password", password);
        ctx.setVariable("fullname", name);

        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true, "UTF-8");

        message.setSubject(subject_new_user);
        message.setTo(to);
        final String htmlContent = this.templateEngine
                .process(EMAIL_NEW_USER_CREDENTIALS, ctx);
        message.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

    @Async
    public void sendUpdatedEmail(String to, String name, String email)
            throws MessagingException, IOException {

        final Context ctx = new Context();
        ctx.setVariable("email", email);
        ctx.setVariable("fullname", name);

        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true, "UTF-8");

        message.setSubject(subject_update_email);
        message.setTo(to);
        final String htmlContent = this.templateEngine
                .process(EMAIL_UPDATED_EMAIL, ctx);
        message.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }
}
