package murach.util;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import murach.business.User;

public final class MailUtil {

    private MailUtil() {
    }

    public static void sendWelcomeEmail(User user)
            throws MessagingException {

        String username = requiredEnvironmentVariable("MAIL_USERNAME");
        String appPassword =
            requiredEnvironmentVariable("MAIL_APP_PASSWORD");

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(
            properties,
            new Authenticator() {
                @Override
                protected PasswordAuthentication
                        getPasswordAuthentication() {
                    return new PasswordAuthentication(
                        username, appPassword);
                }
            }
        );

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipient(
            Message.RecipientType.TO,
            new InternetAddress(user.getEmail())
        );
        message.setSubject("Welcome to our email list");
        message.setContent(
            "<h1>Welcome, " + user.getFirstName() + "!</h1>"
            + "<p>Thank you for joining our email list.</p>",
            "text/html; charset=UTF-8"
        );

        Transport.send(message);
    }

    private static String requiredEnvironmentVariable(String name) {
        String value = System.getenv(name);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                "Missing environment variable: " + name);
        }

        return value;
    }
}