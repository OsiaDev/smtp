package co.cetad.umas.smtp.infrastructure.persistence.adapter;

import co.cetad.umas.smtp.domain.model.dto.EmailNotification;
import co.cetad.umas.smtp.domain.ports.out.EmailPersistencePort;
import co.cetad.umas.smtp.infrastructure.persistence.mapper.EmailNotificationEntityMapper;
import co.cetad.umas.smtp.infrastructure.persistence.postgresql.repository.EmailNotificationR2dbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailPersistenceAdapter implements EmailPersistencePort {

    private final EmailNotificationR2dbcRepository repository;
    private final EmailNotificationEntityMapper mapper;

    @Override
    public CompletableFuture<EmailNotification> save(EmailNotification emailNotification) {
        return CompletableFuture.supplyAsync(() -> mapper.toEntity(emailNotification))
                .thenCompose(entity ->
                        repository.save(entity)
                                .map(mapper::toDomain)
                                .toFuture()
                )
                .whenComplete((result, error) -> {
                    if (error != null) {
                        log.error("Error guardando email: {}", emailNotification.getId(), error);
                    } else {
                        log.debug("Email guardado: {}", result.getId());
                    }
                });
    }

    @Override
    public CompletableFuture<EmailNotification> update(EmailNotification emailNotification) {
        return CompletableFuture.supplyAsync(() -> mapper.toEntity(emailNotification))
                .thenCompose(entity ->
                        repository.save(entity)
                                .map(mapper::toDomain)
                                .toFuture()
                )
                .whenComplete((result, error) -> {
                    if (error != null) {
                        log.error("Error actualizando email: {}", emailNotification.getId(), error);
                    } else {
                        log.debug("Email actualizado: {}", result.getId());
                    }
                });
    }

}




