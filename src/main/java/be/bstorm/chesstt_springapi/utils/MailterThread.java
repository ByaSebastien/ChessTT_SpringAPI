package be.bstorm.chesstt_springapi.utils;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.mail.javamail.JavaMailSender;

public class MailterThread implements Runnable{

    private final JavaMailSender mailSender;

    private final MimeMessage mimeMessage;

    public MailterThread(JavaMailSender mailSender, MimeMessage mimeMessage) {
        this.mailSender = mailSender;
        this.mimeMessage = mimeMessage;
    }

    @Override
    public void run() {

        mailSender.send(mimeMessage);
    }
}
