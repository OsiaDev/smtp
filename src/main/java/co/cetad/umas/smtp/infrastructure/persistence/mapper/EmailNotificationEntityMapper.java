package co.cetad.umas.smtp.infrastructure.persistence.mapper;

import co.cetad.umas.smtp.domain.model.dto.EmailNotification;
import co.cetad.umas.smtp.domain.model.dto.EmailStatus;
import co.cetad.umas.smtp.domain.model.entity.EmailNotificationEntity;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailNotificationEntityMapper {

    private final ObjectMapper objectMapper;

    public EmailNotificationEntity toEntity(EmailNotification domain) {
        return EmailNotificationEntity.builder()
                .id(domain.getId())
                .recipient(domain.getRecipient())
                .subject(domain.getSubject())
                .templateName(domain.getTemplateName())
                .templateVariables(serializeVariables(domain.getTemplateVariables()))
                .status(domain.getStatus().name())
                .createdAt(domain.getCreatedAt())
                .sentAt(domain.getSentAt())
                .errorMessage(domain.getErrorMessage())
                .retryCount(domain.getRetryCount())
                .build();
    }

    public EmailNotification toDomain(EmailNotificationEntity entity) {
        return EmailNotification.builder()
                .id(entity.getId())
                .recipient(entity.getRecipient())
                .subject(entity.getSubject())
                .templateName(entity.getTemplateName())
                .templateVariables(deserializeVariables(entity.getTemplateVariables()))
                .status(EmailStatus.valueOf(entity.getStatus()))
                .createdAt(entity.getCreatedAt())
                .sentAt(entity.getSentAt())
                .errorMessage(entity.getErrorMessage())
                .retryCount(entity.getRetryCount())
                .build();
    }

    private String serializeVariables(Map<String, Object> variables) {
        try {
            return objectMapper.writeValueAsString(variables);
        } catch (JacksonException e) {
            log.error("Error serializando variables", e);
            return "{}";
        }
    }

    private Map<String, Object> deserializeVariables(String variables) {
        try {
            return objectMapper.readValue(variables, new TypeReference<>() {});
        } catch (JacksonException e) {
            log.error("Error deserializando variables", e);
            return new HashMap<>();
        }
    }

}