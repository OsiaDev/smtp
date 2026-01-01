package co.cetad.umas.smtp.infrastructure.message.kafka.mapper;

import co.cetad.umas.smtp.domain.model.dto.DronPreparationMessageDto;
import co.cetad.umas.smtp.domain.model.dto.DronPreparationNotification;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class DronPreparationMessageMapper implements Function<DronPreparationMessageDto, DronPreparationNotification> {

    @Override
    public DronPreparationNotification apply(DronPreparationMessageDto  dto) {
        return DronPreparationNotification.builder()
                .missionId(dto.getMissionId())
                .missionName(dto.getMissionName())
                .vehicleId(dto.getVehicleId())
                .vehicleName(dto.getVehicleName())
                .scheduledExecutionTime(dto.getScheduledExecutionTime())
                .minutesBeforeExecution(dto.getMinutesBeforeExecution())
                .publishedAt(dto.getPublishedAt())
                .recipientEmail(dto.getRecipientEmail())
                .build();
    }

}