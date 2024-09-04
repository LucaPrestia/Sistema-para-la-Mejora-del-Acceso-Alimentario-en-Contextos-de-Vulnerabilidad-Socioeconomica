package ar.utn.sistema;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;

import java.io.IOException;


@Configuration
public class HandlebarsConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(HandlebarsConfig.class);

    @Bean
    public HandlebarsViewResolver handlebarsViewResolver() {
        logger.debug("inicializando handle view");
        HandlebarsViewResolver resolver = new HandlebarsViewResolver();
        resolver.setPrefix("classpath:/templates/"); // classpath toma la ruta de los recursos del sistema (src/main/resources)
        resolver.setSuffix(".hbs");
        resolver.setCache(false);
        resolver.registerHelper("ifCond", (context, options) -> {
            String operator = options.param(0).toString();
            Object value = options.param(1);

            return switch (operator) {
                case "==" -> context.equals(value);
                case "!=" -> !context.equals(value);
                case ">" ->
                        (context instanceof Comparable) && (value instanceof Comparable) && ((Comparable) context).compareTo(value) > 0;
                case "<" ->
                        (context instanceof Comparable) && (value instanceof Comparable) && ((Comparable) context).compareTo(value) < 0;
                default -> throw new IllegalArgumentException("Operador no soportado: " + operator);
            };
        });
        resolver.registerHelper("eq", (context, options) -> {
            logger.debug("Context: " + context);
            logger.debug("Parameter: " + options.param(0));
            if (context == null || options.param(0) == null) {
                return false;
            }
            return context.equals(options.param(0));
        });
        resolver.registerHelper("toString", (context, options) -> {
           return context.toString();
        });
        resolver.registerHelper("isEqual", (context, options) -> {
            return context == options.param(0) ? options.fn(this) : options.inverse(this);
        });
        return resolver;
    }
}
