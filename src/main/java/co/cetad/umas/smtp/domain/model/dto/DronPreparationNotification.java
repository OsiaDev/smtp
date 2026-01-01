package co.cetad.umas.smtp.domain.model.dto;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class DronPreparationNotification {

    String missionId;

    String missionName;

    String vehicleId;

    String vehicleName;

    LocalDateTime scheduledExecutionTime;

    Integer minutesBeforeExecution;

    LocalDateTime publishedAt;

    String recipientEmail;

}