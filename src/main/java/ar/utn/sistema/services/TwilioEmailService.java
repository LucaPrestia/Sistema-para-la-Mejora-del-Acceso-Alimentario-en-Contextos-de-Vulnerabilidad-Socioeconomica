package ar.utn.sistema.services;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Personalization;

import java.io.IOException;

public class TwilioEmailService {

    public void enviarEmail(String EnviarA,String subject, String mensaje)  {
        try{
            Email from = new Email("lprestia@frba.utn.edu.ar");// use your own email address here
            Email to = new Email(EnviarA);

            Content content = new Content("text/html", mensaje);

            Mail mail = new Mail(from, subject, to, content);
            SendGrid sg = new SendGrid("PONER KEY DE LA API");
            Request request = new Request();

            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            System.out.println(response.getStatusCode());
            System.out.println(response.getHeaders());
            System.out.println(response.getBody());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public String toString() {
        return "Mail";
    }
}