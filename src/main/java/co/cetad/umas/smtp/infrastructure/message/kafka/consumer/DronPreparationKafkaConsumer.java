package co.cetad.umas.smtp.infrastructure.message.kafka.consumer;

import co.cetad.umas.smtp.domain.model.dto.DronPreparationMessageDto;
import co.cetad.umas.smtp.domain.ports.in.ProcessDronPreparationUseCase;
import co.cetad.umas.smtp.infrastructure.message.kafka.mapper.DronPreparationMessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class DronPreparationKafkaConsumer {

    private final ProcessDronPreparationUseCase processDronPreparationUseCase;
    private final DronPreparationMessageMapper mapper;

    @KafkaListener(
            topics = "${umas.smtp.kafka.topics.dron-preparation}"
    )
    public void consumeDronPreparationMessage(
            @Payload DronPreparationMessageDto message,
            Acknowledgment acknowledgment) {

        log.info("Mensaje recibido de Kafka");
        log.debug("Contenido: {}", message);

        Optional.ofNullable(message)
                .map(mapper)
                .map(processDronPreparationUseCase::process)
                .orElseGet(() -> CompletableFuture.completedFuture(null))
                .thenAccept(result -> {
                    acknowledgment.acknowledge();
                    log.info("Mensaje procesado y confirmado");
                })
                .exceptionally(error -> {
                    log.error("Error procesando mensaje", error);
                    acknowledgment.acknowledge();
                    return null;
                });
    }

}
