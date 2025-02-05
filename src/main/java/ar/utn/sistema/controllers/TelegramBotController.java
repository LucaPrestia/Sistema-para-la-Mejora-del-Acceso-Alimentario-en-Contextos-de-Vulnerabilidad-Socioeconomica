package ar.utn.sistema.controllers;

import ar.utn.sistema.dto.DTOSesionTelegram;
import ar.utn.sistema.entities.incidente.Incidente;
import ar.utn.sistema.entities.usuarios.Tecnico;
import ar.utn.sistema.entities.usuarios.Usuario;
import ar.utn.sistema.repositories.IncidenteRepository;
import ar.utn.sistema.repositories.TecnicoRepository;
import ar.utn.sistema.repositories.UsuarioRepository;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@BotController
public class TelegramBotController implements TelegramMvcController {

    @Autowired
    public IncidenteRepository incidenteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private TecnicoRepository tecnicoRepository;
    @Value("${bot.token}")
    private String token;

    public static TelegramBot telegramBot;

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
    @MessageRequest("cambiar\\s+zona\\s+de\\s+trabajo\\s+{zona:[a-zA-Z0-9]+}")
    public String cambiarZonaTrabajo(@BotPathVariable("zona") String zona, User user) {
        System.out.println(zona);

        if (loggedInUsers.containsKey(user.id())) {
            updateLastActiveTime(user.id());
            System.out.println(zona);
            try {
                int usuarioMio_id = getIdUsuarioByTelegramId(user.id());
                Usuario usuarioMio = usuarioRepository.findById(usuarioMio_id).get();
                if (usuarioMio != null) {
                    Tecnico tecnicoMio = tecnicoRepository.findByUsuario(usuarioMio).get();

                    if (tecnicoMio != null) tecnicoMio.setAreaCobertura(zona);
                    else return "No se pudo actualizar la zona";
                } else return "No se pudo actualizar la zona";
            } catch (Exception e) {
                return "No se pudo actualizar la zona";
            }
            return "No se pudo actualizar la zona";
        }
        else{
            return  "Debe enviar primero el siguiente mensaje: iniciar sesion 'usuario' 'contraseña'";
        }
    }
    @MessageRequest("Listar incidentes de mi zona")
    public String listarIncidentes(User user) {
        if (loggedInUsers.containsKey(user.id())) {
            updateLastActiveTime(user.id());
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
                    return respuesta;
                } else return "No se pudo devolver lista";
            } catch (Exception e) {
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
            updateLastActiveTime(user.id());  // Actualiza el tiempo de la última actividad
            // Aquí va la lógica para las acciones del usuario
            return new SendMessage(chat.id(), "Puedes realizar las siguientes acciones:\n" +
                    "● Cambiar zona de trabajo 'ZonaDeTrabajoNueva'\n" +
                    "● Registrar una visita a una heladera 'incidenteID'\n" +
                    "● Listar incidentes de mi zona");
        }
        else return new SendMessage(chat.id(),  "Debe enviar primero el siguiente mensaje: iniciar sesion 'usuario' 'contraseña'");
    }
    @MessageRequest("iniciar sesion {usuario:[a-zA-Z0-9]+} {contrasena:[a-zA-Z0-9]+}")
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
}