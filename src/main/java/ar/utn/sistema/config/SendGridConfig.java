package ar.utn.sistema.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class SendGridConfig {

    @Value("${sendgrid.api.key}")
    private String apiKey;

    @Value("${sendwpp.acount.sid}")
    private String acountSid;

    @Value("${sendwpp.auth.token}")
    private String authToken;

}
