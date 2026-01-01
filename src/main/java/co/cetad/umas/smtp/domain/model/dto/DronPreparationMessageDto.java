package co.cetad.umas.smtp.domain.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DronPreparationMessageDto {

    @JsonProperty("mission_id")
    private String missionId;

    @JsonProperty("mission_name")
    private String missionName;

    @JsonProperty("vehicleId")
    private String vehicleId;

    @JsonProperty("vehicleName")
    private String vehicleName;

    @JsonProperty("scheduled_execution_time")
    private LocalDateTime scheduledExecutionTime;

    @JsonProperty("minutes_before_execution")
    private Integer minutesBeforeExecution;

    @JsonProperty("published_at")
    private LocalDateTime publishedAt;

    @JsonProperty("recipient_email")
    private String recipientEmail;

}