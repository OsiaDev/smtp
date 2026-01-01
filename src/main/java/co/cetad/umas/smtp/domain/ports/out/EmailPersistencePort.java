package co.cetad.umas.smtp.domain.ports.out;

import co.cetad.umas.smtp.domain.model.dto.EmailNotification;

import java.util.concurrent.CompletableFuture;

public interface EmailPersistencePort {

    CompletableFuture<EmailNotification> save(EmailNotification emailNotification);

    CompletableFuture<EmailNotification> update(EmailNotification emailNotification);

}