package co.cetad.umas.smtp.infrastructure.persistence.postgresql.repository;

import co.cetad.umas.smtp.domain.model.entity.EmailNotificationEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailNotificationR2dbcRepository extends ReactiveCrudRepository<EmailNotificationEntity, String> {
}