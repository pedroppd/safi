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

    @Value("${spring.mail.sender.address}")
    private String fromAddress;

    @Value("${spring.mail.sender.name}")
    private String senderName;

    @Value("${spring.mail.subject.content}")
    private String subject;

    @Value("${spring.mail.verify.email}")
    private String verifyEmailUrl;

    @Autowired
    private JavaMailSender sender;

    @Async
    public void sendVerificationEmail(User user) throws MessagingException, IOException {
        String verifyUrl = verifyEmailUrl + "?code=" + user.getVerificationCode();
        String userName = user.getFirstName() +" "+ user.getLastName();
        String content = this.buildEmailBody(userName, verifyUrl);
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);
        sender.send(message);
    }

    public String buildEmailBody(String userName, String targetLink) {
        String emailBody = "Querido %s,\n" +
                "<br> Por favor, clique no link abaixo para verificar seu cadastro:<br>\n" +
                "<h3><a href=\"%s\" target=\"_blank\">VERIFICAR CADASTRO !</a></h3>\n" +
                "Obrigado,\n" +
                "<br> SAFI company";
        return String.format(emailBody, userName, targetLink);
    }
}
