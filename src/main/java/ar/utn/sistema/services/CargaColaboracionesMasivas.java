package ar.utn.sistema.services;

import ar.utn.sistema.entities.colaboracion.*;
import ar.utn.sistema.entities.notificacion.Contacto;
import ar.utn.sistema.entities.notificacion.MedioNotificacion;
import ar.utn.sistema.entities.notificacion.Notificacion;
import ar.utn.sistema.entities.usuarios.ColaboradorFisico;
import ar.utn.sistema.entities.usuarios.TipoDocumento;
import ar.utn.sistema.entities.usuarios.Usuario;
import ar.utn.sistema.repositories.ColaboradorRepository;
import ar.utn.sistema.repositories.NotificacionRepository;
import ar.utn.sistema.repositories.UsuarioRepository;
import ar.utn.sistema.utils.CodigoGenerador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CargaColaboracionesMasivas {
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private CoeficientesColaboracionService sCoeficientes;
    @Autowired
    private ColaboradorRepository rColaborador;
    @Autowired
    private UsuarioRepository rUsuario;
    @Autowired
    private NotificacionRepository notificacionRepository;

    public CargaColaboracionesMasivas(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Transactional
    public List<Colaboracion> cargarColaboracionesMasivamente(MultipartFile archivoCsv){
        List<Colaboracion> colaboraciones = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(archivoCsv.getInputStream(), StandardCharsets.UTF_8))) {
            String linea;
            br.readLine();
            AtomicInteger nuevoUsuario = new AtomicInteger();
            StringBuilder mensaje = new StringBuilder();

            while ((linea = br.readLine()) != null) {
                nuevoUsuario.set(0);
                String[] campos = linea.split(",");

                if (campos.length == 8) {
                    String tipoDoc = campos[0];
                    String documento = campos[1];
                    String nombre = campos[2];
                    String apellido = campos[3];
                    String mail = campos[4];
                    String fechaColaboracionStr = campos[5];
                    String formaColaboracion = campos[6];
                    int cantidad = Integer.parseInt(campos[7]);

                    Optional<ColaboradorFisico> colaboradorRegistrado = rColaborador.findByTipoDocumentoAndDocumento(TipoDocumento.valueOf(tipoDoc), documento);

                    ColaboradorFisico colaborador = colaboradorRegistrado.orElseGet(() -> {
                        nuevoUsuario.set(1);
                        ColaboradorFisico nuevoColaborador = new ColaboradorFisico(nombre, apellido, TipoDocumento.valueOf(tipoDoc), documento);
                        Contacto contacto = new Contacto(MedioNotificacion.EMAIL, mail);
                        nuevoColaborador.agregarContacto(contacto);

                        String username;
                        do {
                            username = CodigoGenerador.generarCodigoUnico();
                        } while (rUsuario.findByUsuario(username).isPresent());

                        Usuario usuario = new Usuario(username, passwordEncoder.encode(documento), "COLABORADOR_FISICO");
                        usuario.setNuevo(2);
                        nuevoColaborador.setUsuario(usuario);

                        mensaje.append("¡Gracias por tu valiosa colaboración! Tu esfuerzo ha contribuido significativamente a mejorar el acceso alimentario para quienes más lo necesitan.\n\n")
                                .append("Hemos registrado tu colaboración y ahora tienes acceso a nuestra plataforma para que puedas seguir ayudando en el futuro. A continuación, te brindamos tus credenciales de acceso:\n\n")
                                .append("Usuario: ").append(username).append("\n")
                                .append("Contraseña: ").append(documento).append("\n\n")
                                .append("Además, te recordamos que aún queda espacio para ")
                                .append("Recuerda que los puntos acumulados por tus donaciones pueden ser canjeados por servicios disponibles en nuestra plataforma.\n\n")
                                .append("¡Gracias nuevamente por ser parte de esta causa! Juntos podemos hacer la diferencia.");
                        return nuevoColaborador;
                    });

                    LocalDate fechaColaboracion = convertirFecha(fechaColaboracionStr);
                    TipoColaboracionEnum tipoColaboracionEnum = TipoColaboracionEnum.valueOf(formaColaboracion);

                    Colaboracion colaboracion = getColaboracion(tipoColaboracionEnum, cantidad, fechaColaboracion);
                    colaborador.agregarColaboracion(colaboracion);  // esto ya se encarga de sumar los puntos
                    // no hace falta persistir las colaboraciones o los contactos del colaborador ya que el save de colaborador ya se encarga de realizarlo por el método cascada asignado en cada relación desde la clase Colaborador
                    rColaborador.save(colaborador);
                    if (nuevoUsuario.get() == 1){
                        Notificacion notificacion = new Notificacion();
                        notificacion.setMensaje(mensaje.toString());
                        colaborador.notificar(notificacion);
                        notificacion.setMensaje("Carga Masiva creacion de usuario");
                        notificacionRepository.save(notificacion);
                    }
                    colaboraciones.add(colaboracion);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al procesar el archivo CSV", e);
        }
        return colaboraciones;
    }

    private LocalDate convertirFecha(String fechaStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[M/d/yyyy][MM/d/yyyy][M/dd/yyyy][MM/dd/yyyy]");
        return LocalDate.parse(fechaStr, formatter);
    }

    private Colaboracion getColaboracion(TipoColaboracionEnum tipoColaboracionEnum, int cantidad, LocalDate fecha) {
        Colaboracion colaboracion;
        switch (tipoColaboracionEnum) {
            case DINERO ->{
                colaboracion = new ColaboracionDinero((float) cantidad, fecha, sCoeficientes.obtenerCoeficiente("DINERO")); // SUPONGO UNICA
                colaboracion.setViejo(true);
            }
            case DONACION_VIANDAS ->{
                colaboracion = new ColaboracionVianda(cantidad, fecha, sCoeficientes.obtenerCoeficiente("DONACION_VIANDAS"));
                colaboracion.setViejo(true);
            }
            case REDISTRIBUCION_VIANDAS ->{
                colaboracion = new ColaboracionDistribucionViandas(cantidad, fecha, sCoeficientes.obtenerCoeficiente("REDISTRIBUCION_VIANDAS"));
                colaboracion.setViejo(true);
            }
            case ENTREGA_TARJETAS ->{
                colaboracion = new ColaboracionTarjeta(cantidad, fecha, sCoeficientes.obtenerCoeficiente("ENTREGA_TARJETAS"));
                colaboracion.setViejo(true);
            }

            default -> colaboracion = null;
        }
        return colaboracion;
    }
}
