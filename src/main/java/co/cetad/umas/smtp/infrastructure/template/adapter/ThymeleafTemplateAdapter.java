package co.cetad.umas.smtp.infrastructure.template.adapter;

import co.cetad.umas.smtp.domain.ports.out.TemplateProcessorPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class ThymeleafTemplateAdapter implements TemplateProcessorPort {

    private final TemplateEngine templateEngine;

    @Override
    public CompletableFuture<String> processTemplate(String templateName, Map<String, Object> variables) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.debug("Procesando template: {} con variables: {}", templateName, variables.keySet());

                Context context = new Context();
                context.setVariables(variables);

                String html = templateEngine.process(templateName, context);

                log.debug("Template procesado exitosamente: {}", templateName);
                return html;

            } catch (Exception e) {
                log.error("Error procesando template {}: {}", templateName, e.getMessage(), e);
                throw new RuntimeException("Error al procesar template: " + templateName, e);
            }
        });
    }

}