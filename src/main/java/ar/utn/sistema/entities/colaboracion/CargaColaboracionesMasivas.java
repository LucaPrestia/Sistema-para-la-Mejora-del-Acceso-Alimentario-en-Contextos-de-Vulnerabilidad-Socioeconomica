package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.notificacion.Contacto;
import ar.utn.sistema.entities.notificacion.MedioNotificacion;
import ar.utn.sistema.entities.usuarios.ColaboradorFisico;
import ar.utn.sistema.entities.usuarios.TipoDocumento;
import ar.utn.sistema.utils.LectorArchivo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CargaColaboracionesMasivas {
    private String dir;
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

                // TODO: corroborar existencia de colaborador en la base de datos! por ahora lo creo de una:
                ColaboradorFisico colaborador = new ColaboradorFisico(nombre, apellido, TipoDocumento.valueOf(tipoDoc),documento);
                Contacto contacto = new Contacto(MedioNotificacion.EMAIL, mail);
                colaborador.agregarContacto(contacto);

                String fechaColaboracionStr = campos[5];
                String formaColaboracion = campos[6];
                int cantidad = Integer.parseInt(campos[7]);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[d][dd]/[M][MM]/yyyy");
                LocalDate fechaColaboracion = LocalDate.parse(fechaColaboracionStr, formatter);
                TipoColaboracion tipoColaboracion = TipoColaboracion.valueOf(formaColaboracion);

                Colaboracion colaboracion = getColaboracion(tipoColaboracion, cantidad, fechaColaboracion);
                colaborador.agregarColaboracion(colaboracion); // esto ya se encarga de sumar los puntos
                // TODO: persistir en la base de datos
                colaboraciones.add(colaboracion);

            }
        }
        return colaboraciones;
    }

    private static Colaboracion getColaboracion(TipoColaboracion tipoColaboracion, int cantidad, LocalDate fecha) {
        Colaboracion colaboracion;
        switch (tipoColaboracion) {
            case DINERO ->{
                colaboracion = new ColaboracionDinero((float) cantidad, fecha); // SUPONGO UNICA
                colaboracion.setViejo(true);
            }
            case DONACION_VIANDAS ->{
                colaboracion = new ColaboracionVianda(cantidad, fecha);
                colaboracion.setViejo(true);
            }
            case REDISTRIBUCION_VIANDAS ->{
                colaboracion = new ColaboracionDistribucionViandas(cantidad, fecha);
                colaboracion.setViejo(true);
            }
            case ENTREGA_TARJETAS ->{
                colaboracion = new ColaboracionTarjeta(cantidad, fecha);
                colaboracion.setViejo(true);
            }

            default -> colaboracion = null;
        }
        return colaboracion;
    }
}
