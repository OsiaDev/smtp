package co.cetad.umas.smtp.application.usecase;

import co.cetad.umas.smtp.domain.model.dto.DronPreparationNotification;
import co.cetad.umas.smtp.domain.model.dto.EmailNotification;
import co.cetad.umas.smtp.domain.model.dto.EmailStatus;
import co.cetad.umas.smtp.domain.ports.in.ProcessDronPreparationUseCase;
import co.cetad.umas.smtp.domain.ports.out.EmailPersistencePort;
import co.cetad.umas.smtp.domain.ports.out.EmailSenderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessDronPreparationUseCaseImpl implements ProcessDronPreparationUseCase {

    private final EmailSenderPort emailSenderPort;
    private final EmailPersistencePort emailPersistencePort;

    @Value("${umas.smtp.templates.dron-preparation.subject}")
    private String subjectTemplate;

    @Value("${umas.smtp.templates.dron-preparation.template-name}")
    private String templateName;

    @Override
    public CompletableFuture<Void> process(DronPreparationNotification notification) {
        log.info("Procesando notificación de preparación de dron para misión: {}", notification.getMissionId());

        return CompletableFuture
                .supplyAsync(() -> buildEmailNotification(notification))
                .thenCompose(this::persistAndSendEmail)
                .thenAccept(result -> log.info("Notificación procesada exitosamente: {}", notification.getMissionId()))
                .exceptionally(error -> {
                    log.error("Error procesando notificación: {}", notification.getMissionId(), error);
                    return null;
                });
    }

    private EmailNotification buildEmailNotification(DronPreparationNotification notification) {
        String subject = subjectTemplate.replace("{missionName}",
                notification.getMissionName() != null ? notification.getMissionName() : "Sin nombre");

        Map<String, Object> variables = Map.of(
                "missionId", notification.getMissionId(),
                "missionName", notification.getMissionName() != null ? notification.getMissionName() : "Sin nombre",
                "vehicleId", notification.getVehicleId() != null ? notification.getVehicleId() : "N/A",
                "vehicleName", notification.getVehicleName() != null ? notification.getVehicleName() : "N/A",
                "scheduledExecutionTime", notification.getScheduledExecutionTime() != null ?
                        notification.getScheduledExecutionTime().toString() : "No programado",
                "minutesBeforeExecution", notification.getMinutesBeforeExecution() != null ?
                        notification.getMinutesBeforeExecution() + " minutos" : "N/A",
                "publishedAt", notification.getPublishedAt() != null ?
                        notification.getPublishedAt().toString() : LocalDateTime.now().toString()
        );

        return EmailNotification.builder()
                .id(UUID.randomUUID().toString())
                .recipient(notification.getRecipientEmail())
                .subject(subject)
                .templateName(templateName)
                .templateVariables(variables)
                .status(EmailStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .retryCount(0)
                .build();
    }

    private CompletableFuture<EmailNotification> persistAndSendEmail(EmailNotification emailNotification) {
        return emailPersistencePort.save(emailNotification)
                .thenCompose(savedEmail ->
                        emailSenderPort.sendEmail(savedEmail)
                                .thenCompose(emailPersistencePort::update)
                                .exceptionally(error -> handleSendError(savedEmail, error))
                );
    }

    private EmailNotification handleSendError(EmailNotification emailNotification, Throwable error) {
        log.error("Error enviando email: {}", emailNotification.getId(), error);
        EmailNotification failedEmail = emailNotification.markAsFailed(error.getMessage());

        emailPersistencePort.update(failedEmail)
                .exceptionally(persistError -> {
                    log.error("Error persistiendo fallo: {}", emailNotification.getId(), persistError);
                    return null;
                });

        return failedEmail;
    }

}