package ar.utn.sistema.services;

import ar.utn.sistema.entities.colaboracion.*;
import ar.utn.sistema.entities.notificacion.Contacto;
import ar.utn.sistema.entities.notificacion.MedioNotificacion;
import ar.utn.sistema.entities.usuarios.ColaboradorFisico;
import ar.utn.sistema.entities.usuarios.TipoDocumento;
import ar.utn.sistema.entities.usuarios.Usuario;
import ar.utn.sistema.repositories.ColaboradorRepository;
import ar.utn.sistema.repositories.UsuarioRepository;
import ar.utn.sistema.utils.CodigoGenerador;
import ar.utn.sistema.utils.LectorArchivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CargaColaboracionesMasivas {

    @Autowired
    private CoeficientesColaboracionService sCoeficientes;

    @Autowired
    private ColaboradorRepository rColaborador;

    @Autowired
    private UsuarioRepository rUsuario;

    private String dir; // todo: colaboraciones.csv tiene la fecha en formato mm/dd/aaaa, hay que cambiar las fechas para que tengan formato dd/mm/aaaq
    public List<Colaboracion> cargarColaboracionesMasivamente(){
        LectorArchivo lector = new LectorArchivo();
        List<Colaboracion> colaboraciones = new ArrayList<>();
        String linea;

        lector.traerLinea(dir); //  saco el header

        while ((linea = lector.traerLinea(dir)) != null) {
            String[] campos = linea.split(",");

            if (campos.length == 8) {
                String tipoDoc = campos[0];
                String documento = campos[1];
                String nombre = campos[2];
                String apellido = campos[3];
                String mail = campos[4];

                Optional<ColaboradorFisico> colaboradorRegistrado = rColaborador.findByTipoDocumentoAndDocumento(TipoDocumento.valueOf(tipoDoc), documento);
                ColaboradorFisico colaborador = colaboradorRegistrado.orElseGet(() -> {
                    ColaboradorFisico nuevoColaborador = new ColaboradorFisico(nombre, apellido, TipoDocumento.valueOf(tipoDoc), documento);

                    Contacto contacto = new Contacto(MedioNotificacion.EMAIL, mail);
                    nuevoColaborador.agregarContacto(contacto);

                    String username;
                    do {
                        username = CodigoGenerador.generarCodigoUnico();
                    } while (rUsuario.findById(Integer.valueOf(username)).isPresent());

                    Usuario usuario = new Usuario(username, documento, "colaborador"); // por default el documento es la contraseña
                    nuevoColaborador.setUsuario(usuario);

                    /* TODO: lo del mail(?
                        Una vez procesado el archivo, se le deberá enviar un mail a aquellos colaboradores que no tenían usuario
                        en el sistema previamente agradeciendo el aporte realizado y brindándole credenciales de acceso por si desea ingresar
                        al sistema. Una vez que ingrese por primera vez deberá confirmar que sus datos sean correctos y completar los datos faltantes.
                     */
                    return nuevoColaborador;
                });

                String fechaColaboracionStr = campos[5];
                String formaColaboracion = campos[6];
                int cantidad = Integer.parseInt(campos[7]);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[d][dd]/[M][MM]/yyyy");
                LocalDate fechaColaboracion = LocalDate.parse(fechaColaboracionStr, formatter);
                TipoColaboracionEnum tipoColaboracionEnum = TipoColaboracionEnum.valueOf(formaColaboracion);

                Colaboracion colaboracion = getColaboracion(tipoColaboracionEnum, cantidad, fechaColaboracion);
                colaborador.agregarColaboracion(colaboracion); // esto ya se encarga de sumar los puntos
                // no hace falta persistir las colaboraciones o los contactos del colaborador ya que el save de colaborador ya se encarga de realizarlo por el método cascada asignado en cada relación desde la clase Colaborador
                rColaborador.save(colaborador);
                colaboraciones.add(colaboracion);

            }
        }
        return colaboraciones;
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
