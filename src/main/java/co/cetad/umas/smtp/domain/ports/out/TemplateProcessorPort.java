package co.cetad.umas.smtp.domain.ports.out;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@FunctionalInterface
public interface TemplateProcessorPort {

    CompletableFuture<String> processTemplate(String templateName, Map<String, Object> variables);

}