package ar.utn.sistema.controllers;

import ar.utn.sistema.dto.DTOEsperaSesionIncidenteFoto;
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
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
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
    public static TelegramBotController instancia;
    public TelegramBotController() {
        // Asegurate de usar tu token correcto aquí

        telegramBot = new TelegramBot("7713772704:AAEfTB0uHPBWjx9NkclJjnK1UrmxzlGiUGE");
        instancia=this;

    }
    public static TelegramBotController getInstance() {
        if(instancia == null) {
            instancia = new TelegramBotController();
        }
        return instancia;
    }
    public void enviarMensaje(Integer id_usuario, String mensaje) {

        long chat_id = 0L;
        DTOSesionTelegram dto = obtenerSesionConUsuario(id_usuario);
        loggedInUsers.forEach(x->System.out.println(x.id_usuario));
        System.out.println(id_usuario);
        if(dto!=null) {
            chat_id = dto.getChat_id();
        }
        System.out.println("finalize con el tecnico para mandar, chat : "+ chat_id + "total logged in: "+ loggedInUsers.size() );

        telegramBot.execute(new SendMessage(chat_id, mensaje));
        System.out.println("Mensaje enviado a tecnico: " + mensaje);

    }

    // Lista para almacenar las sesiones activas de los usuarios
    private List<DTOEsperaSesionIncidenteFoto> esperandoFoto = new ArrayList<>();
    private List<DTOSesionTelegram> loggedInUsers = new ArrayList<>();
    private Map<Long, Long> lastActiveTimes = new HashMap<>();  // Mapa de actividad del usuario
    private static final long INACTIVITY_TIMEOUT = 5 * 60 * 1000;  // 5 minutos en milisegundos


    @Autowired
    private PasswordEncoder passwordEncoder;

    public DTOSesionTelegram obtenerSesionConTelegram(long id_usuario) {
        for( DTOSesionTelegram dto : loggedInUsers){
            if(dto.id_telegram== id_usuario){
                return dto;
            }
        }
        return null;
    }
    public DTOSesionTelegram obtenerSesionConUsuario(long id_usuario) {
        for( DTOSesionTelegram dto : loggedInUsers){
            if(dto.id_usuario== id_usuario){
                return dto;
            }
        }
        return null;
    }
    @Override
    public String getToken() {
        return token;
    }
    @BotRequest(value = "/hello", type = {MessageType.CALLBACK_QUERY, MessageType.MESSAGE})
    public BaseRequest hello(User user, Chat chat) {
        return new SendMessage(chat.id(), "Hello, " + user.firstName() + "!");
    }
    @MessageRequest("Registrar una visita a una heladera {incidenteID:[a-zA-Z0-9]+} solucionado: {solucionado:[a-zA-Z0-9]+} descripcion de trabajo: {trabajo:.+}")
    public String registrarVisita(@BotPathVariable("incidenteID") String incidente_id,
                                  @BotPathVariable("solucionado") String solucionado,
                                  @BotPathVariable("trabajo") String descripcionTrabajo,
                                  TelegramRequest request,
                                  User user,
                                  Chat chat) {

        DTOSesionTelegram dto = this.obtenerSesionConTelegram(user.id());
        if (dto==null) {
            return "Debe iniciar sesión primero enviando el siguiente mensaje: 'Iniciar sesion usuario contraseña'";
        }

        try {
            int usuarioMio_id = dto.getId_usuario();
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
            DTOEsperaSesionIncidenteFoto dto2 = new DTOEsperaSesionIncidenteFoto();
            dto2.setDescripcion(descripcionTrabajo);
            dto2.setId_telegram(user.id());
            dto2.setChat_id(chat.id());
            dto2.setIncidente(incidente);
            dto2.setTecnico(tecnicoMio);
            System.out.println(solucionado);
            dto2.setSolucionado(solucionado.compareToIgnoreCase("si")==0);
            esperandoFoto.add(dto2);

            return "Por favor, envíe una foto con la evidencia del trabajo realizado.";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al registrar la visita: " + e.getMessage();
        }
    }

    @BotRequest(value = "")
    public synchronized BaseRequest mandarFoto(User user, Chat chat, Message message) throws InterruptedException {
        // Verifica si el usuario tiene una visita pendiente

        DTOEsperaSesionIncidenteFoto dtoMio = esperandoFoto.stream()
                .filter(dto -> dto.getId_telegram() == user.id())
                .findFirst()
                .orElse(null);

        if (dtoMio == null) {
            return new SendMessage(chat.id(), "No registraste la visita primero.");
        }

        // Verifica si el mensaje contiene una foto
        if (message.photo() == null || message.photo().length == 0) {
            return new SendMessage(chat.id(), "No se recibió ninguna foto. Por favor, envíe una imagen adjunta.");
        }

        // Obtiene la mejor calidad de la foto
        String fileId = message.photo()[message.photo().length - 1].fileId();

        // Descarga la imagen desde Telegram
        byte[] photoBytes = descargarFotoTelegram(fileId);

        if (photoBytes == null) {
            return new SendMessage(chat.id(), "Error al descargar la imagen. Intente nuevamente.");
        }

        // Guarda la visita con la foto
        VisitaIncidente visitaIncidente = new VisitaIncidente(
                dtoMio.getIncidente(),
                dtoMio.getTecnico(),
                dtoMio.getDescripcion(),
                photoBytes,
                dtoMio.getSolucionado()
        );

        visitaIncidenteRepository.save(visitaIncidente);
        esperandoFoto.remove(dtoMio); // Remueve la entrada después de guardar
        if(dtoMio.getSolucionado()){
            dtoMio.getIncidente().setEstado("RESUELTO");
        }
        incidenteRepository.save(dtoMio.getIncidente());
        return new SendMessage(chat.id(), "Visita registrada correctamente con la foto.");
    }
    @MessageRequest("Cambiar zona de trabajo {zona:[a-zA-Z0-9]+}")
    public String cambiarZonaTrabajo(@BotPathVariable("zona") String zona, User user) {
        DTOSesionTelegram dto = this.obtenerSesionConTelegram(user.id());

        if (dto!=null) {
            System.out.println(zona);
            try {
                int usuarioMio_id = dto.getId_usuario();
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
        DTOSesionTelegram dto = this.obtenerSesionConTelegram(user.id());
        if (dto!=null) {
            try {
                int usuarioMio_id = dto.getId_usuario();
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
        DTOSesionTelegram dto = this.obtenerSesionConTelegram(user.id());

        if (dto!=null) {
            updateLastActiveTime(user.id());  // Actualiza el tiempo de la última actividad
            return new SendMessage(chat.id(), "Bienvenido nuevamente, " + user.firstName() + ". ¿Qué deseas hacer?");
        }

        // Si no está logueado
        return new SendMessage(chat.id(), "Hola, para continuar necesitas hacer login. Envíame tu nombre de usuario.");
    }

    @MessageRequest("Listar acciones")  // Captura cualquier mensaje
    public BaseRequest loginOrAction(User user, Chat chat, String message) {
        // Verificar si el usuario está logueado
        DTOSesionTelegram dto = this.obtenerSesionConTelegram(user.id());

        if (dto!=null) {
            return new SendMessage(chat.id(), "Puedes realizar las siguientes acciones:\n" +
                    "● Cambiar zona de trabajo 'ZonaDeTrabajoNueva'\n" +
                    "● Registrar una visita a una heladera 'incidenteID' solucionado: 'si/no' descripcion de trabajo: 'descripcion' \n" +
                    "● Listar incidentes de mi zona");
        }
        else return new SendMessage(chat.id(),  "Debe enviar primero el siguiente mensaje: iniciar sesion 'usuario' 'contraseña'");
    }
    @MessageRequest("Iniciar sesion {usuario:[a-zA-Z0-9]+} {contrasena:[a-zA-Z0-9]+}")
    public String iniciarSesion(@BotPathVariable("usuario") String usuario,@BotPathVariable("contrasena") String contrasena, User user,Chat chat) {
        DTOSesionTelegram dto = this.obtenerSesionConTelegram(user.id());

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
                    sesion.setChat_id(chat.id());
                    loggedInUsers.add(sesion);  // Guarda la sesión en el mapa
                    System.out.println("tamaño:"+loggedInUsers.size());
                    return "Login exitoso. Bienvenido, " + username + ". ¿Qué deseas hacer?";
                }
            }
            return "Usuario o contraseña incorrectos. Intenta de nuevo.";

        } else {
            return "Usuario o contraseña incorrectos. Intenta de nuevo.";
        }
    }


    private byte[] descargarFotoTelegram(String fileId) {
        try {
            GetFile getFile = new GetFile(fileId);
            File file = telegramBot.execute(getFile).file();
            if (file == null) {
                return null;
            }

            // Construir la URL de descarga
            String filePath = file.filePath();
            String url = "https://api.telegram.org/file/bot" + token + "/" + filePath;

            // Descargar la imagen
            InputStream in = new URL(url).openStream();
            System.out.println("casi la traje");
            return in.readAllBytes();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isInactive(Long userId) {
        Long lastActiveTime = lastActiveTimes.get(userId);
        return lastActiveTime != null && (System.currentTimeMillis() - lastActiveTime > INACTIVITY_TIMEOUT);
    }

    private void updateLastActiveTime(Long userId) {
        lastActiveTimes.put(userId, System.currentTimeMillis());
    }


}