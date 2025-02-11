package ar.utn.sistema.services;

import ar.utn.sistema.config.SendGridConfig;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.stereotype.Service;

@Service
public class SendWpp  {
    // Find your Account Sid and Token at twilio.com/console
    private final String account_sid;
    private final String auth_token;

    public SendWpp(SendGridConfig sendGridConfig) {
        this.account_sid = sendGridConfig.getAcountSid();
        this.auth_token = sendGridConfig.getAuthToken();
    }
    public void enviarAPersona(String telefono, String mensaje){
        enviarWpp(telefono,mensaje);
    }
    public void enviarWpp(String EnviarA, String mensaje) {
        try {
            Twilio.init(account_sid, auth_token);
            Message message = Message.creator(
                            new com.twilio.type.PhoneNumber("whatsapp:"+EnviarA),
                            new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
                            mensaje)

                    .create();

            System.out.println(message.getSid());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Whatsapp";
    }

}
