package ar.utn.sistema;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.github.jknack.handlebars.springmvc.HandlebarsViewResolver;


@Configuration
public class HandlebarsConfig implements WebMvcConfigurer {

    @Bean
    public HandlebarsViewResolver handlebarsViewResolver() {
        HandlebarsViewResolver resolver = new HandlebarsViewResolver();
        resolver.setPrefix("classpath:/templates/"); // classpath toma la ruta de los recursos del sistema (src/main/resources)
        resolver.setSuffix(".hbs");
        resolver.setCache(false);
        return resolver;
    }

}
