package br.com.safi.services;

import br.com.safi.models.User;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class MailService {


    @Value("${spring.mail.content.path}")
    private String filePath;

    @Value("${spring.mail.sender.address}")
    private String fromAddress;

    @Value("${spring.mail.sender.name}")
    private String senderName;

    @Value("${spring.mail.recipient.address}")
    private String toAddress;

    @Value("${spring.mail.subject.content}")
    private String subject;

    @Value("${spring.mail.verify.email}")
    private String verifyEmailUrl;



    @Autowired
    private JavaMailSender sender;

    @Async
    public void sendVerificationEmail(User user) throws MessagingException, IOException {
        String verifyUrl = verifyEmailUrl + "?code=" + user.getVerificationCode();
        String content = this.buildEmailBody(user.getEmail(), verifyUrl);
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);
        sender.send(message);
    }

    public String buildEmailBody(String userName, String targetLink) throws IOException {
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder builder = new StringBuilder();
        String st;
        while ((st = br.readLine()) != null) {
            builder.append(st);
        }
        return String.format(builder.toString(), userName, targetLink);
    }
}
