package co.cetad.umas.smtp.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "email_notifications")
public class EmailNotificationEntity {

    @Id
    private String id;

    @Column("recipient")
    private String recipient;

    @Column("subject")
    private String subject;

    @Column("template_name")
    private String templateName;

    @Column("template_variables")
    private String templateVariables;

    @Column("status")
    private String status;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("sent_at")
    private LocalDateTime sentAt;

    @Column("error_message")
    private String errorMessage;

    @Column("retry_count")
    private Integer retryCount;

}