package co.cetad.umas.smtp.domain.ports.out;

import co.cetad.umas.smtp.domain.model.dto.EmailNotification;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface EmailSenderPort {

    CompletableFuture<EmailNotification> sendEmail(EmailNotification emailNotification);

}