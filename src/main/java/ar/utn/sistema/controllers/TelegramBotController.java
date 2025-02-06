package ar.utn.sistema.controllers;

import ar.utn.sistema.dto.DTOSesionTelegram;
import ar.utn.sistema.entities.incidente.Incidente;
import ar.utn.sistema.entities.incidente.VisitaIncidente;
import ar.utn.sistema.entities.usuarios.Tecnico;
import ar.utn.sistema.entities.usuarios.Usuario;
import ar.utn.sistema.repositories.IncidenteRepository;
import ar.utn.sistema.repositories.TecnicoRepository;
import ar.utn.sistema.repositories.UsuarioRepository;
import ar.utn.sistema.repositories.VisitaIncidenteRepository;
import com.github.kshashov.telegram.api.MessageType;
import com.github.kshashov.telegram.api.TelegramMvcController;
import com.github.kshashov.telegram.api.TelegramRequest;
import com.github.kshashov.telegram.api.bind.annotation.BotController;
import com.github.kshashov.telegram.api.bind.annotation.BotPathVariable;
import com.github.kshashov.telegram.api.bind.annotation.BotRequest;
import com.github.kshashov.telegram.api.bind.annotation.request.MessageRequest;
import com.github.kshashov.telegram.config.TelegramBotGlobalProperties;
import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.*;
/* PARA MANDAR MENSAJE DESDE CUALQUIER CLASE
TelegramBotController bot = TelegramBotController.telegramBot;
bot.execute(new SendMessage(chatId, "message"));
*/

@BotController
public class TelegramBotController implements TelegramMvcController {

    @Autowired
    public IncidenteRepository incidenteRepository;
    @Autowired
    public VisitaIncidenteRepository visitaIncidenteRepository;
    @Autowired
    public UsuarioRepository usuarioRepository;
    @Autowired
    public TecnicoRepository tecnicoRepository;
    @Value("${bot.token}")
    private String token;
    public static TelegramBot telegramBot;

    public TelegramBotController() {
        // Asegurate de usar tu token correcto aquí
        telegramBot = new TelegramBot("7713772704:AAEfTB0uHPBWjx9NkclJjnK1UrmxzlGiUGE");
    }
    public void enviarMensaje(Integer id_usuario, String mensaje) {
        long telegram_id = getTelegramIdByUsuarioId(id_usuario);
        telegramBot.execute(new SendMessage(telegram_id, mensaje));


    }
    // Lista para almacenar las sesiones activas de los usuarios
    private Map<Long, DTOSesionTelegram> loggedInUsers = new HashMap<>();
    private Map<Long, Long> lastActiveTimes = new HashMap<>();  // Mapa de actividad del usuario
    private static final long INACTIVITY_TIMEOUT = 5 * 60 * 1000;  // 5 minutos en milisegundos
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String getToken() {
        return token;
    }
    @MessageRequest("Registrar una visita a una heladera {incidenteID:[a-zA-Z0-9]+} solucionado: {solucionado:[a-zA-Z0-9]+} descripcion de trabajo: {trabajo:.+}")
    public String registrarVisita(@BotPathVariable("incidenteID") String incidente_id,
                                  @BotPathVariable("solucionado") String solucionado,
                                  @BotPathVariable("trabajo") String descripcionTrabajo,
                                  TelegramRequest request,
                                  User user,
                                  Chat chat) {

        if (loggedInUsers.containsKey(user.id())) {
            try {
                int usuarioMio_id = getIdUsuarioByTelegramId(user.id());
                Usuario usuarioMio = usuarioRepository.findById(usuarioMio_id).orElse(null);

                if (usuarioMio == null) {
                    return "Error: No se encontró el usuario asociado.";
                }

                Tecnico tecnicoMio = tecnicoRepository.findByUsuario(usuarioMio).orElse(null);
                if (tecnicoMio == null) {
                    return "Error: No se encontró el técnico asociado al usuario.";
                }

                Optional<Incidente> incidenteOpt = incidenteRepository.findById(Integer.parseInt(incidente_id));

                if (!incidenteOpt.isPresent()) {
                    return "Error: No se encontró el incidente con el ID proporcionado.";
                }

                Incidente incidente = incidenteOpt.get();

                // Configura el callback para esperar una imagen
                request.setCallback(new Callback() {
                    @Override
                    public void onResponse(BaseRequest req, BaseResponse res) {
                        if (req.getFileName() != null) {
                            byte[] photoBytes = request.getMessage().photo()[0].fileId().getBytes();

                            VisitaIncidente visitaIncidente;
                            if(Objects.equals(solucionado, "si")){
                               visitaIncidente = new VisitaIncidente(incidente,tecnicoMio,descripcionTrabajo,photoBytes,true);
                            }else{
                                visitaIncidente = new VisitaIncidente(incidente,tecnicoMio,descripcionTrabajo,photoBytes,false);
                            }

                            visitaIncidenteRepository.save(visitaIncidente);

                            telegramBot.execute(new SendMessage(chat.id(), "La visita ha sido registrada exitosamente."));
                        } else {
                            telegramBot.execute(new SendMessage(chat.id(), "No se recibió ninguna foto. Por favor, intente de nuevo."));
                        }
                    }

                    @Override
                    public void onFailure(BaseRequest req, IOException e) {
                        telegramBot.execute(new SendMessage(user.id(), "Ocurrió un error al recibir la foto. Intente nuevamente."));
                    }
                });

                return "Por favor, envíe una foto de cómo quedó el trabajo realizado.";

            } catch (Exception e) {
                e.printStackTrace();
                return "Error al registrar la visita: " + e.getMessage();
            }
        } else {
            return "Debe iniciar sesión primero enviando el siguiente mensaje: 'Iniciar sesion usuario contraseña'";
        }
    }

    @MessageRequest("Cambiar zona de trabajo {zona:[a-zA-Z0-9]+}")
    public String cambiarZonaTrabajo(@BotPathVariable("zona") String zona, User user) {

        if (loggedInUsers.containsKey(user.id())) {
            System.out.println(zona);
            try {
                int usuarioMio_id = getIdUsuarioByTelegramId(user.id());
                Usuario usuarioMio = usuarioRepository.findById(usuarioMio_id).get();
                System.out.println(usuarioMio.getUsuario());
                if (usuarioMio != null) {
                    Tecnico tecnicoMio = tecnicoRepository.findByUsuario(usuarioMio).get();
                    System.out.println(tecnicoMio.getUsuario() + "");
                    tecnicoMio.setAreaCobertura(zona) ;
                    tecnicoRepository.save(tecnicoMio);
                    return "Zona de trabajo actualizada correctamente";
                } else return "No se pudo actualizar la zona";
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return "No se pudo actualizar la zona";
            }
        }
        else{
            return  "Debe enviar primero el siguiente mensaje: iniciar sesion 'usuario' 'contraseña'";
        }
    }
    @MessageRequest("Listar incidentes de mi zona")
    public String listarIncidentes(User user) {
        if (loggedInUsers.containsKey(user.id())) {
            try {
                int usuarioMio_id = getIdUsuarioByTelegramId(user.id());
                Usuario usuarioMio = usuarioRepository.findById(usuarioMio_id).get();
                if (usuarioMio != null) {
                    Tecnico tecnicoMio = tecnicoRepository.findByUsuario(usuarioMio).get();
                    List<Incidente> incidentes = incidenteRepository.findAllByZona(tecnicoMio.getAreaCobertura());
                    String respuesta = "";
                    for (Incidente incidente : incidentes) {
                        respuesta = respuesta.concat(incidente.getTexto() + "\n");
                    }
                    if(respuesta.length()>1){
                        return respuesta;
                    }
                    else return "No hay incidentes en tu zona";
                } else return "No se pudo devolver lista";
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return "No se pudo devolver lista";
            }
        }
        else{
            return  "Debe enviar primero el siguiente mensaje: iniciar sesion 'usuario' 'contraseña'";
        }
    }


    @BotRequest(value = "/start", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest start(User user, Chat chat) {
        // Verificar si el usuario está logueado
        if (loggedInUsers.containsKey(user.id())) {
            updateLastActiveTime(user.id());  // Actualiza el tiempo de la última actividad
            return new SendMessage(chat.id(), "Bienvenido nuevamente, " + user.firstName() + ". ¿Qué deseas hacer?");
        }

        // Si no está logueado
        return new SendMessage(chat.id(), "Hola, para continuar necesitas hacer login. Envíame tu nombre de usuario.");
    }

    @MessageRequest("Listar acciones")  // Captura cualquier mensaje
    public BaseRequest loginOrAction(User user, Chat chat, String message) {
        // Verificar si el usuario está logueado
        if (loggedInUsers.containsKey(user.id())) {
            return new SendMessage(chat.id(), "Puedes realizar las siguientes acciones:\n" +
                    "● Cambiar zona de trabajo 'ZonaDeTrabajoNueva'\n" +
                    "● Registrar una visita a una heladera 'incidenteID' solucionado: 'si/no' descripcion de trabajo: 'descripcion' \n" +
                    "● Listar incidentes de mi zona");
        }
        else return new SendMessage(chat.id(),  "Debe enviar primero el siguiente mensaje: iniciar sesion 'usuario' 'contraseña'");
    }
    @MessageRequest("Iniciar sesion {usuario:[a-zA-Z0-9]+} {contrasena:[a-zA-Z0-9]+}")
    public String iniciarSesion(@BotPathVariable("usuario") String usuario,@BotPathVariable("contrasena") String contrasena, User user,Chat chat) {

        String username = usuario;
        String password =contrasena;
        System.out.println(username+password);
        System.out.println(passwordEncoder.encode(password));
        // Verifica si las credenciales son correctas
        Optional<List<Usuario>> usuarioOpt = usuarioRepository.findAllByUsuario(username );

        if (usuarioOpt.isPresent()) {
            List<Usuario> usuarioList = usuarioOpt.get();
            for (Usuario usuario1 : usuarioList) {
                if(passwordEncoder.matches(password,usuario1.getContrasena())){
                    DTOSesionTelegram sesion = new DTOSesionTelegram();
                    sesion.setId_telegram(user.id());
                    sesion.setId_usuario(usuario1.getId());

                    loggedInUsers.put(user.id(), sesion);  // Guarda la sesión en el mapa
                    updateLastActiveTime(user.id());
                    return "Login exitoso. Bienvenido, " + username + ". ¿Qué deseas hacer?";
                }
            }
            return "Usuario o contraseña incorrectos. Intenta de nuevo.";

        } else {
            return "Usuario o contraseña incorrectos. Intenta de nuevo.";
        }
    }


    private boolean isInactive(Long userId) {
        Long lastActiveTime = lastActiveTimes.get(userId);
        return lastActiveTime != null && (System.currentTimeMillis() - lastActiveTime > INACTIVITY_TIMEOUT);
    }

    private void updateLastActiveTime(Long userId) {
        lastActiveTimes.put(userId, System.currentTimeMillis());
    }
    public int getIdUsuarioByTelegramId(long idTelegram) {
        DTOSesionTelegram sesion = loggedInUsers.get(idTelegram);
        if (sesion != null) {
            return sesion.getId_usuario();  // Devuelve el id_usuario del DTO
        } else {
            // Retorna un valor predeterminado o lanza una excepción si no se encuentra el usuario
            throw new IllegalArgumentException("Usuario no encontrado para el id de Telegram: " + idTelegram);
        }
    }
    public long getTelegramIdByUsuarioId(int usuarioId) {
        for (Map.Entry<Long, DTOSesionTelegram> entry : loggedInUsers.entrySet()) {
            if (entry.getValue().getId_usuario() == usuarioId) {
                return entry.getKey();  // Retorna el id de Telegram asociado al usuario
            }
        }
        throw new IllegalArgumentException("Telegram ID no encontrado para el usuario ID: " + usuarioId);
    }
}