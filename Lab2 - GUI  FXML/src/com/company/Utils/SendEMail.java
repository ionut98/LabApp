package com.company.Utils;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class SendEMail{


        public static void sendEMail(String email, String text){

            try{

            String host = "smtp.gmail.com";
            final String username = "catedramap@gmail.com";
            final String password = "catedramap1234";
            String to = email;
            String from = "catedramap@gmail.com";
            String subject = "Atentionare note!";
            String messageText = text;
            boolean sessionDebug = false;

            Properties properties = System.getProperties();

            properties.put("mail.smtp.auth","true");
            properties.put("mail.smtp.starttls.enable","true");
            properties.put("mail.smtp.host",host);
            properties.put("mail.smtp.port","587");
            properties.put("mail.smtp.starttls.required","true");

         //   java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            Session mailSession = Session.getDefaultInstance(properties,null);
            mailSession.setDebug(sessionDebug);

            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            message.setRecipients(Message.RecipientType.TO,address);
            message.setSubject(subject);
            message.setSentDate(new Date());
            message.setText(messageText);

            Transport transport = mailSession.getTransport("smtp");
            transport.connect(host,username,password);
            transport.sendMessage(message,message.getAllRecipients());
            transport.close();
            System.out.println("Your message has been sent!");


            } catch (AddressException e) {
                System.out.println(e.getMessage());
            } catch (MessagingException e) {
                System.out.println(e.getMessage());
            }

        }


}
