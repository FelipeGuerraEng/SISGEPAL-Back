package com.SISGEPAL.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
import org.thymeleaf.templateresolver.StringTemplateResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Collections;


@Service
public class MailingService {

    private static final String EMAIL_NEW_USER_CREDENTIALS
            = "classpath:/mail/html/send_credentials/new_user_credentials.html";

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private TemplateEngine templateEngine;

    @Async
    public void sendCredentialEmail(String subject, String to, String name, String username,
                                    String password) throws MessagingException {

        final Context ctx = new Context();
        ctx.setVariable("email.credentials.user", username);
        ctx.setVariable("email.credentials.password", password);
        ctx.setVariable("email.credentials.name", name);

        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true, "UTF-8");

        message.setSubject(subject);
        message.setTo(to);
        final String htmlContent = this.templateEngine.process(EMAIL_NEW_USER_CREDENTIALS, ctx);
        message.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

    @Bean
    public TemplateEngine emailTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        return templateEngine;
    }

    private ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(Integer.valueOf(2));
        templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
        templateResolver.setPrefix("/mail/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);
        return templateResolver;
    }
}
