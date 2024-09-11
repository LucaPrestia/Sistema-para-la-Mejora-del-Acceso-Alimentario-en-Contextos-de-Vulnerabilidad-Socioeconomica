package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.usuarios.Colaborador;
import ar.utn.sistema.utils.LectorArchivo;

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
                String fechaColaboracion = campos[5];
                String formaColaboracion = campos[6];
                int cantidad = Integer.parseInt(campos[7]);
                TipoColaboracion tipoColaboracion = TipoColaboracion.valueOf(formaColaboracion);
                Colaboracion colaboracion = getColaboracion(tipoColaboracion, cantidad);
                //TODO CHEQUEAR CONTRA LA BASE DE DATOS Y AVISAR + AGREGAR COLABORACION AL USUARIO
                colaboraciones.add(colaboracion);
            }
        }
        return colaboraciones;
    }

    private static Colaboracion getColaboracion(TipoColaboracion tipoColaboracion, int cantidad) {
        Colaboracion colaboracion;
        switch (tipoColaboracion) {
            case DONACION_DINERO ->{
                colaboracion = new ColaboracionDinero(cantidad);//SUPONGO UNICA
                colaboracion.setViejo(true);
                 }
            case DONACION_VIANDA ->{
                colaboracion = new ColaboracionVianda();
                colaboracion.setViejo(true);
            }
            case DISTRUBUCION_VIANDA ->{
                colaboracion = new ColaboracionDistribucionViandas(cantidad);
                colaboracion.setViejo(true);
            }
            case ENTREGA_TARJETAS ->{
                colaboracion = new ColaboracionTarjeta(cantidad);
                colaboracion.setViejo(true);
            }

            default -> colaboracion = null;
        }
        return colaboracion;
    }
}
