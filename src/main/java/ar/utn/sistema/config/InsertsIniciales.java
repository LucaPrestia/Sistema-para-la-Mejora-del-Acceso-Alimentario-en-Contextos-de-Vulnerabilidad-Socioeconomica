package ar.utn.sistema.config;

import ar.utn.sistema.entities.colaboracion.TipoColaboracionEnum;
import ar.utn.sistema.entities.configuracion.CoeficientesColaboracion;
import ar.utn.sistema.entities.configuracion.ColaboradorColaboracion;
import ar.utn.sistema.entities.configuracion.ParametrosGenerales;
import ar.utn.sistema.entities.configuracion.TipoColaboracion;
import ar.utn.sistema.entities.usuarios.*;
import ar.utn.sistema.repositories.AdminRepository;
import ar.utn.sistema.repositories.ColaboradorRepository;
import ar.utn.sistema.repositories.configuracion.CoeficientesColaboracionRepository;
import ar.utn.sistema.repositories.configuracion.ColaboradorColaboracionRepository;
import ar.utn.sistema.repositories.configuracion.ParametrosGeneralesRepository;
import ar.utn.sistema.repositories.configuracion.TipoColaboracionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class InsertsIniciales {
    private Logger logger = LoggerFactory.getLogger(InsertsIniciales.class);
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Autowired
    private ColaboradorColaboracionRepository rConfigColaboracion;

    @Autowired
    private ParametrosGeneralesRepository rParametro;

    @Autowired
    private TipoColaboracionRepository rTipoColaboracion;

    @Autowired
    private CoeficientesColaboracionRepository rCoeficientes;

    @Autowired
    private AdminRepository rAdmin;

    @Autowired
    private ColaboradorRepository rColaborador;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
                rConfigColaboracion.save(colaboradorPersonaHumana);
                ColaboradorColaboracion colaboradorPersonaJuridica = new ColaboradorColaboracion("PERSONA_JURIDICA", tiposPersonaJuridica);
                rConfigColaboracion.save(colaboradorPersonaJuridica);

                // ESTO POR AHORA CARGA TABLAS: colaborador_colaboracion_tipo_colaboracion, colaborador_colaboracion y tipo_colaboracion
                // chequear los selects en la base, en mi caso fue bien!!

                // Inserta parámetros generales, como el tiempoMovimientoApertura para las tarjetas!
                // todo: evaluar si quedan pendiente ingresar otros parámetros generales
                ParametrosGenerales par1 = new ParametrosGenerales("TIEMPO_MOVIMIENTO_APERTURA", new BigDecimal(3));
                rParametro.save(par1);

                // Inserta coeficientes para puntos por cada colaboracion:
                CoeficientesColaboracion coef1 = new CoeficientesColaboracion(tipo1, 0.5);
                CoeficientesColaboracion coef2 = new CoeficientesColaboracion(tipo2, 1.5);
                CoeficientesColaboracion coef3 = new CoeficientesColaboracion(tipo3, 1.0);
                CoeficientesColaboracion coef4 = new CoeficientesColaboracion(tipo4, 2.0);
                CoeficientesColaboracion coef5 = new CoeficientesColaboracion(tipo5, 5.0);
                CoeficientesColaboracion coef6 = new CoeficientesColaboracion(tipo6, 0.0);

                rCoeficientes.saveAll(List.of(coef1, coef2, coef3, coef4, coef5, coef6));

                // agrego usuarios por default
                Usuario admin = new Usuario("admin",passwordEncoder.encode("admin123"),"ADMIN");
                admin.setNuevo(0); // el administrador no necesita onboarding!!!!
                Ong ong = new Ong("ONG Sistema Mejora Acceso Alimentario", admin);
                rAdmin.save(ong);

                Usuario colaboradorUsr = new Usuario("colaboradorP",passwordEncoder.encode("colaboradorP123"),"COLABORADOR_FISICO");
                Colaborador colaboradorP = new ColaboradorFisico("colaborador", "prueba", TipoDocumento.DNI, "11111111");
                colaboradorP.setUsuario(colaboradorUsr);

                Usuario colaboradorJrd = new Usuario("colaboradorJ",passwordEncoder.encode("colaboradorJ123"),"COLABORADOR_JURIDICO");
                Colaborador colaboradorJ  = new ColaboradorJuridico("razon social", "GASTRONOMIA", "27444444444",TipoJuridico.EMPRESA);
                colaboradorJ.setUsuario(colaboradorJrd);

                rColaborador.saveAll(List.of(colaboradorP, colaboradorJ));

                logger.info("todos los inserts iniciales fueron bien!!");
            }
        };
    }
}
