package be.bstorm.chesstt_springapi.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
@Slf4j
public class MailerUtils {

    @Value("${spring.mail.username}")
    private String appEmailAddress;

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public void send(String title, String templateName, Context context, String... to) {
        String htmlContent = templateEngine.process("emails/" + templateName, context);
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(appEmailAddress);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(htmlContent, true);

//            javaMailSender.send(mimeMessage);
            MailterThread mailterThread = new MailterThread(javaMailSender,mimeMessage);
            mailterThread.run();

        } catch (MessagingException e) {
            log.error(e.getMessage());
            throw new MailSendException("Failed to send email");
        }
    }
}
