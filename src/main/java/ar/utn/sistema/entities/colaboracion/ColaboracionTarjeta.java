package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.usuarios.ColaboradorFisico;
import ar.utn.sistema.entities.usuarios.PersonaVulnerable;
import ar.utn.sistema.utils.CodigoGenerador;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Getter@Setter
public class ColaboracionTarjeta extends Colaboracion {
    private List<Tarjeta> tarjetasAsignadas = new ArrayList<Tarjeta>();
    private int cantidadTarjetasAsignadas;
    private int cantidadTarjetasRepartidas;
    private Boolean es_vieja;
    public ColaboracionTarjeta(int cantidadTarjetasRecibidas) {
        super(TipoColaboracion.ENTREGA_TARJETAS, 2.0);
        this.cantidadTarjetasAsignadas = cantidadTarjetasRecibidas;
        this.cantidadTarjetasRepartidas = 0;
        for (int i = 0; i < this.cantidadTarjetasAsignadas; i++) {
            this.tarjetasAsignadas.add(new Tarjeta(CodigoGenerador.generarCodigoUnico()));
        }
    }

    public ColaboracionTarjeta(int cantidadCargaMasiva, LocalDate fechaCargaMasiva) {
        super(TipoColaboracion.ENTREGA_TARJETAS, 2.0);
        this.cantidadTarjetasRepartidas = cantidadCargaMasiva;
        this.setFechaColaboracion(fechaCargaMasiva);
        // como no hay info de las tarjetas en la carga masiva, no se carga creo
        /*for (int i = 0; i < cantidad; i++) {
            this.tarjetasCreadas.add(new Tarjeta());
        }*/
    }

    @Override
    public double sumarPuntos(){
        return this.getCoeficientePuntos() * this.cantidadTarjetasRepartidas;
        // esto va ok sólo en carga masiva
        // para cargas no masivas se tiene que ir sumando puntos gradualmente, a medida que el colaborador va entregando tarjetas!

    }

    public Tarjeta registrarPersonaVulnerable(PersonaVulnerable persona, ColaboradorFisico colaborador){
        // en un form de alta se van a cargar los datos de la persona vulnerable a partir de esta colaboración vigente (quedan tarjetas sin repartir)
        Tarjeta tarjeta = this.tarjetasAsignadas.stream().filter(t -> t.getPersonaVulnerable() == null ).findFirst().get();
        tarjeta.setPersonaVulnerable(persona);
        tarjeta.setRegistrador(colaborador);
        colaborador.actualizarPuntos(this.getCoeficientePuntos());
        return tarjeta; // TODO: se debe persistir en la base todos los cambios!!!
    }

}
