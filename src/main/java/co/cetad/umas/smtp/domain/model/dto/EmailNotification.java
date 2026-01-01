package co.cetad.umas.smtp.domain.model.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Map;

@Value
@Builder(toBuilder = true)
public class EmailNotification {
    String id;
    String recipient;
    String subject;
    String templateName;
    Map<String, Object> templateVariables;
    EmailStatus status;
    LocalDateTime createdAt;
    LocalDateTime sentAt;
    String errorMessage;
    Integer retryCount;

    public EmailNotification markAsSent() {
        return toBuilder()
                .status(EmailStatus.SENT)
                .sentAt(LocalDateTime.now())
                .build();
    }

    public EmailNotification markAsFailed(String errorMessage) {
        return toBuilder()
                .status(EmailStatus.FAILED)
                .errorMessage(errorMessage)
                .retryCount(retryCount != null ? retryCount + 1 : 1)
                .build();
    }

    public boolean canRetry() {
        return retryCount == null || retryCount < 3;
    }

}