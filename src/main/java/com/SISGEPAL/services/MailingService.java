package com.SISGEPAL.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Collections;

@Service
public class MailingService {

    private static final String EMAIL_NEW_USER_CREDENTIALS
            = "html/send_credentials/new_user_credentials";

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;


    @Async
    public void sendCredentialEmail(String subject, String to, String name, String username,
                                    String password) throws MessagingException, IOException {

        final Context ctx = new Context();
        ctx.setVariable("username", username);
        ctx.setVariable("password", password);
        ctx.setVariable("fullname", name);

        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true, "UTF-8");

        message.setSubject(subject);
        message.setTo(to);
        final String htmlContent = this.templateEngine
                .process(EMAIL_NEW_USER_CREDENTIALS, ctx);
        message.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }
}
