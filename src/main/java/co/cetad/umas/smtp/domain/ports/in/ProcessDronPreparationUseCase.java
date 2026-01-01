package co.cetad.umas.smtp.domain.ports.in;

import co.cetad.umas.smtp.domain.model.dto.DronPreparationNotification;

import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface ProcessDronPreparationUseCase {

    CompletableFuture<Void> process(DronPreparationNotification notification);

}