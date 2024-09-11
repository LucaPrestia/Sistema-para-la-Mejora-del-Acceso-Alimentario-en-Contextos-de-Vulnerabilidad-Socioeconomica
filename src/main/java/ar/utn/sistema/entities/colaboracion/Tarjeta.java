package ar.utn.sistema.entities.colaboracion;

import ar.utn.sistema.entities.heladera.Heladera;
import ar.utn.sistema.entities.usuarios.PersonaVulnerable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
@Getter @Setter
public class Tarjeta {
    private String codigo;
    private PersonaVulnerable personaVulnerable;
    private List<MovimientoTarjeta>  movimientos = new ArrayList<MovimientoTarjeta>();
    public Tarjeta() {}
    public int usosDeTarjetaHoy(){
        int contador = 0;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        Date hoy = new Date();
        List<Date> fechasDeUso = movimientos.stream().map(x->x.getFechaUso()).toList();
        for (Date fecha : fechasDeUso) {
            cal1.setTime(hoy);
            cal2.setTime(fecha);
            if (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)) {
                contador++;
            }
        }
        return contador;
    }
    public boolean puedoUsarTarjeta(){
        return this.usosDeTarjetaHoy() < personaVulnerable.getMenoresACargo() * 2 + 4 ;
    }
    public boolean UsarTarjeta(Heladera heladera){
        boolean se_puede = puedoUsarTarjeta();
        if(se_puede){
            this.movimientos.add(new MovimientoTarjeta(heladera));
        }
        return se_puede;
    }

}
