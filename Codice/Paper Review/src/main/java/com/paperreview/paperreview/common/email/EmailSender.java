package com.paperreview.paperreview.common.email;

import com.paperreview.paperreview.common.DotenvUtil;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailSender {

    public static void sendEmail(MailBase mail) throws MessagingException {
        final String from = DotenvUtil.getEmailSender();                // tua email
        final String password = DotenvUtil.getEmailPassword();         // usa una password per app

        // Configurazione SMTP per Gmail (puoi cambiare provider)
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getTo()));
        message.setSubject(mail.getSubject());
        message.setContent(mail.getBody(), "text/html;charset=utf-8");

        Transport.send(message);
        System.out.println("email inviata con successo.");
    }
}
