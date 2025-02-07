package ar.utn.sistema.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;


public class SendWpp  {
    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID = "a";
    public static final String AUTH_TOKEN = "a";
    public void enviarAPersona(String telefono, String mensaje){
        enviarWpp(telefono,mensaje);
    }
    public void enviarWpp(String EnviarA, String mensaje) {
        try {
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
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
