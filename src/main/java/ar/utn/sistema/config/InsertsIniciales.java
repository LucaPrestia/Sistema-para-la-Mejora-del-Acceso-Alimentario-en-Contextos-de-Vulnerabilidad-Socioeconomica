package ar.utn.sistema.config;

import ar.utn.sistema.entities.configuracion.ColaboradorColaboracion;
import ar.utn.sistema.entities.configuracion.TipoColaboracion;
import ar.utn.sistema.repositories.configuracion.ColaboradorColaboracionRepository;
import ar.utn.sistema.repositories.configuracion.ParametrosGeneralesRepository;
import ar.utn.sistema.repositories.configuracion.TipoColaboracionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class InsertsIniciales {
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Autowired
    private ColaboradorColaboracionRepository rColaborador;

    @Autowired
    private ParametrosGeneralesRepository rParametro;

    @Autowired
    private TipoColaboracionRepository rTipoColaboracion;

    @Bean
    public CommandLineRunner initData() {
        return (args) -> {
            // solo ejecutamos la inserción de datos si ddl-auto es create o create-drop (esto para evitar la duplicacion de datos)
            if ("create".equalsIgnoreCase(ddlAuto) || "create-drop".equalsIgnoreCase(ddlAuto)) {
                // Crear TipoColaboracion para "PERSONA_HUMANA"
                TipoColaboracion tipo1 = new TipoColaboracion( "DINERO", "Donación Dinero");
                TipoColaboracion tipo2 = new TipoColaboracion("DONACION_VIANDAS", "Donación Viandas");
                TipoColaboracion tipo3 = new TipoColaboracion("REDISTRIBUCION_VIANDAS","Distribución Viandas");
                TipoColaboracion tipo4 = new TipoColaboracion("ENTREGA_TARJETAS","Registro de Personas Vulnerables - Repartición Tarjetas");
                TipoColaboracion tipo5 = new TipoColaboracion("GESTION_HELADERA","Gestión de Heladera");
                TipoColaboracion tipo6 = new TipoColaboracion("OFERTA_SERVICIO","Prestar Servicio para Canje");

                // Guardar TipoColaboracion en la base de datos
                rTipoColaboracion.saveAll(List.of(tipo1, tipo2, tipo3, tipo4, tipo5, tipo6));

                // Crear listas de TipoColaboracion para cada ColaboradorColaboracion
                List<TipoColaboracion> tiposPersonaHumana = List.of(tipo1, tipo2, tipo3, tipo4);
                List<TipoColaboracion> tiposPersonaJuridica = List.of(tipo5, tipo6, tipo1);

                // Crear las entidades ColaboradorColaboracion
                ColaboradorColaboracion colaboradorPersonaHumana = new ColaboradorColaboracion("PERSONA_HUMANA", tiposPersonaHumana);
                rColaborador.save(colaboradorPersonaHumana);
                ColaboradorColaboracion colaboradorPersonaJuridica = new ColaboradorColaboracion("PERSONA_JURIDICA", tiposPersonaJuridica);
                rColaborador.save(colaboradorPersonaJuridica);

                // ESTO POR AHORA CARGA TABLAS: colaborador_colaboracion_tipo_colaboracion, colaborador_colaboracion y tipo_colaborador
                // chequear los selects en la base, en mi caso fue bien!!

                // todo: insertar parámetros generales, como el tiempoMovimientoApertura para las tarjetas!
                // todo: insertar coeficientes para las colaboraciones
            }
        };
    }
}
