package co.cetad.umas.smtp.infrastructure.email.adapter;

import co.cetad.umas.smtp.domain.model.dto.EmailNotification;
import co.cetad.umas.smtp.domain.ports.out.EmailSenderPort;
import co.cetad.umas.smtp.domain.ports.out.TemplateProcessorPort;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmtpEmailAdapter implements EmailSenderPort {

    private final JavaMailSender mailSender;
    private final TemplateProcessorPort templateProcessor;

    @Value("${umas.smtp.email.from.address}")
    private String fromAddress;

    @Value("${umas.smtp.email.from.name}")
    private String fromName;

    @Override
    public CompletableFuture<EmailNotification> sendEmail(EmailNotification emailNotification) {
        log.info("Iniciando envío de email: {}", emailNotification.getId());

        return templateProcessor
                .processTemplate(emailNotification.getTemplateName(), emailNotification.getTemplateVariables())
                .thenCompose(htmlContent -> sendMimeMessage(emailNotification, htmlContent))
                .thenApply(sent -> emailNotification.markAsSent())
                .exceptionally(error -> {
                    log.error("Error enviando email {}: {}", emailNotification.getId(), error.getMessage());
                    throw new RuntimeException("Error al enviar email", error);
                });
    }

    private CompletableFuture<Void> sendMimeMessage(EmailNotification emailNotification, String htmlContent) {
        return CompletableFuture.runAsync(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom(fromAddress, fromName);
                helper.setTo(emailNotification.getRecipient());
                helper.setSubject(emailNotification.getSubject());
                helper.setText(htmlContent, true);
                helper.setSentDate(java.sql.Timestamp.valueOf(LocalDateTime.now()));

                mailSender.send(message);
                log.info("Email enviado exitosamente: {}", emailNotification.getId());

            } catch (Exception e) {
                log.error("Error al crear/enviar mensaje MIME: {}", e.getMessage(), e);
                throw new RuntimeException("Error al crear mensaje MIME", e);
            }
        });
    }

}