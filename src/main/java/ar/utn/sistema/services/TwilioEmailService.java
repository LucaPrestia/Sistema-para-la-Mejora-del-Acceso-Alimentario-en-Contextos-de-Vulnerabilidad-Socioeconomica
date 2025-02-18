package ar.utn.sistema.services;
import ar.utn.sistema.config.SendGridConfig;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Personalization;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TwilioEmailService {
    private final String apiKey;

    public TwilioEmailService(SendGridConfig sendGridConfig) {
        this.apiKey = sendGridConfig.getApiKey();
    }

    public void enviarEmail(String EnviarA,String subject, String mensaje) throws IOException {
        Email from = new Email("venerusb@gmail.com");
        Email to = new Email(EnviarA);
        Content content = new Content("text/plain", mensaje);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }
    }
    @Override
    public String toString() {
        return "Mail";
    }
}